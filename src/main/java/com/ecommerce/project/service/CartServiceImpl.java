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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCardId(cartId,productId);
        if(cartItem!=null)
        {
            throw new APIException("Product"+product.getProductName()+"already exits" +
                    "in the cart");
        }

        if(product.getQuantity()==0)
        {
            throw new APIException(product.getProductName()+"is not available!");
        }
        if(product.getQuantity()<quantity)
        {
            throw new APIException("Please, make an order of the "+product.getProductName()
            +"less than or equal to the quantity "+product.getQuantity()+".");
        }

        CartItem newCartItem=new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        product.setQuantity(product.getQuantity());
        cart.setTotalPrice(cart.getTotalPrice()+(product.getSpecialPrice()*quantity));
        cartRepository.save(cart);

        CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
        List<CartItem>cartItems=cart.getCartItems();
        Stream<ProductDTO>productStream=cartItems.stream().map(item->
        {
           ProductDTO map=modelMapper.map(item.getProduct(),ProductDTO.class);
           map.setQuantity(item.getQuantity());
        });
        //Perform validations: like product is already in the cart, if product stock is present
        //Create cart items
        //save cart item
        //return updated cart
    }

    private Cart createCart() {

        Cart userCart=cartRepository.findCartByEmail(authUtil.loggedEmail());
        if(userCart!=null)
        {
            return userCart;
        }

        Cart cart=new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        return cartRepository.save(cart);

    }
}
