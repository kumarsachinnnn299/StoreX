package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.repositories.CartItemRepository;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.security.response.MessageResponse;
import com.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        //Find existing cart or create one
        Cart cart=createCart();
        //Retrieve product details
        Product product=productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product", "productId"
                ,productId));

        //Perform validations: like product is already in the cart, if product stock is present
        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(),productId);
        if(cartItem!=null)
        {
            throw new APIException("Product "+product.getProductName()+" already exits" +
                    "in the cart");
        }

        if(product.getQuantity()==0)
        {
            throw new APIException(product.getProductName()+" is not available!");
        }
        if(product.getQuantity()<quantity)
        {
            throw new APIException("Please, make an order of the "+product.getProductName()
            +" less than or equal to the quantity "+product.getQuantity()+".");
        }

        //Create cart item
        CartItem newCartItem=new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        //save cart item
        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity());
        cart.setTotalPrice(cart.getTotalPrice()+(product.getSpecialPrice()*quantity));
        cartRepository.save(cart);

        //return updated cart
        CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
        List<CartItem>cartItems=cart.getCartItems();
        Stream<ProductDTO>productStream=cartItems.stream().map(item->
        {
           ProductDTO map=modelMapper.map(item.getProduct(),ProductDTO.class);
           map.setQuantity(item.getQuantity());
           return map;
        });

        cartDTO.setProducts(productStream.toList());
        return cartDTO;


    }



    private Cart createCart() {

        Cart userCart=cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart!=null)
        {
            return userCart;
        }

        Cart cart=new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        return cartRepository.save(cart);

    }
    @Override
    public List<CartDTO> getAllCarts() {

        List<Cart>carts=cartRepository.findAll();
        if(carts.isEmpty())
        {
            throw  new APIException("No Cart exists!!");
        }
        List<CartDTO>cartDTOS=carts.stream().map(
                cart->{
                    CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
                    List<ProductDTO>products=cart.getCartItems().stream()
                            .map(p->modelMapper.map(p.getProduct(),ProductDTO.class))
                                    .collect(Collectors.toList());
                    cartDTO.setProducts(products);
                    return cartDTO;
                }
        ).collect(Collectors.toList());
        return cartDTOS;

    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart=cartRepository.findCartByEmailAndCartId(emailId,cartId);
        if(cart==null)
        {
            throw new ResourceNotFoundException("Cart","cartId",cartId);
        }
        CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
        cart.getCartItems().forEach(c->
                c.getProduct().setQuantity(c.getQuantity()));//This is done
        /*
            because if we don't do it , it is getting the quantity of products from the original stock of
            products that we initially set. Here we are setting the quantity to be taken from the cart
        * */
        List<ProductDTO>products=cart.getCartItems().stream()
                .map(p-> modelMapper.map(p.getProduct(),ProductDTO.class))
                .toList();
        cartDTO.setProducts(products);
        return  cartDTO;
    }
    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String emailId=authUtil.loggedInEmail();
        Cart userCart=cartRepository.findCartByEmail(emailId);
        Long cartId=userCart.getCartId();

        Cart cart=cartRepository.findById(cartId)
                .orElseThrow(()-> new ResourceNotFoundException("Cart", "cartId", cartId));
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product", "product id", productId));

        if(product.getQuantity()==0)
        {
            throw new APIException(product.getProductName()+" is not available!!");
        }

        if(product.getQuantity()<quantity)
        {
            throw new APIException("Please make an order of the product "+product.getProductName()
            +" less than or equal to the quantity "+product.getQuantity()+".");
        }
        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);
        if(cartItem==null)
        {
            throw new APIException("Product "+ product.getProductName()+" not available in the cart!!");
        }
        int newQuantity=cartItem.getQuantity()+quantity;
        if(newQuantity<0)
        {
            throw new APIException("The resulting quantity can't be negative!!");
        }
        if(newQuantity==0)
        {
            deleteProductFromCart(cartId,productId);
        }
        else {

            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice()) * quantity);
            cartRepository.save(cart);
        }
        CartItem updatedItem=cartItemRepository.save(cartItem);
        if(updatedItem.getQuantity()==0)
        {
            cartItemRepository.deleteById(updatedItem.getCartItemId());
        }

        CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
        List<CartItem>cartItems=cart.getCartItems();

        /*This below method is done
            because if we don't do it , it is getting the quantity of products from the original stock of
            products that we initially set. Here we are setting the quantity to be taken from the cart
         */
        Stream<ProductDTO>productDTOStream=cartItems.stream().map(item->{
                ProductDTO prd=modelMapper.map(cartItem.getProduct(),ProductDTO.class);
                prd.setQuantity(item.getQuantity());
                return prd;
        });
        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }

    @Override
    @Transactional
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart=cartRepository.findById(cartId)
                .orElseThrow(()->new ResourceNotFoundException("Cart", "cartId",cartId));
        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);
        if(cartItem==null)
        {
            throw new ResourceNotFoundException("Product","productId",productId);
        }
        cart.setTotalPrice(cart.getTotalPrice()-(cartItem.getProductPrice()*cartItem.getQuantity()));
        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId,productId);
        return "Product "+cartItem.getProduct().getProductName()+" removed from the cart!";
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart=cartRepository.findById(cartId)
                .orElseThrow(()-> new ResourceNotFoundException("Cart", "cartId",cartId));
        Product product=productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));
        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(productId,cartId);

        if(cartItem==null)
        {
            throw new APIException("Product "+product.getProductName()+" not available in the cart!!");
        }
        //Removes the current cost of the item from the cart total cost
        double cartPrice=cart.getTotalPrice()-(cartItem.getProductPrice()*cartItem.getQuantity());

        //Updating the latest price of that cartItem
        cartItem.setProductPrice(product.getSpecialPrice());

        //Adding the updated price of cartitem to the total cost of cart
        cart.setTotalPrice(cartPrice+(cartItem.getProductPrice()*cartItem.getQuantity()));
        cartItem=cartItemRepository.save(cartItem);

    }


}
