package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FileService fileService;

    @Value("${project.image}")//This will get its value from application.properties
    private String path;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService;


    @Override
    public ProductDTO add(ProductDTO productDTO, Long categoryId) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
        //These three inputs of resourceNotFOundException are given because this is defined in this way
        // by default. You can check it by expanding ResourceNotFoundException class

        //Checking if product is already present
        boolean isProductNotPresent=true;
        List<Product>products=category.getProducts();
        for (int i = 0; i < products.size(); i++) {
            if(products.get(i).getProductName().equals(productDTO.getProductName()))
            {
                isProductNotPresent=false;
                break;
            }
        }

        if(isProductNotPresent) {
            Product product = modelMapper.map(productDTO, Product.class);
            product.setCategory(category);
            product.setImage("default.png");
            double specialPrice = product.getPrice() - (product.getDiscount() * 0.01 * product.getPrice());
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        }
        else{
            throw  new APIException("Product already exist!!");
        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> productPage=productRepository.findAll(pageDetails);

//        List<Product>products=productRepository.findAll();: when we implement we ignore this way

        List<Product>products=productPage.getContent();

        if(products.isEmpty())
        {
            throw new APIException("No products present!! Kindly add a product..");
        }
       List<ProductDTO>productDTOS=products.stream()
               .map(product -> modelMapper.map(product,ProductDTO.class))
               .toList();
       ProductResponse productResponse=new ProductResponse();
       productResponse.setContent(productDTOS);
       productResponse.setPageNumber(productPage.getNumber());
       productResponse.setPageSize(productPage.getSize());
       productResponse.setTotalElements(productPage.getTotalElements());
       productResponse.setTotalPages(productPage.getTotalPages());
       productResponse.setLastPage(productPage.isLast());

       return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));

        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> productPage=productRepository.findByCategoryOrderByPriceAsc(category,pageDetails);
//        List<Product> products=productRepository.findByCategoryOrderByPriceAsc(category); : This was used when paging was not implemented
        List<Product>products=productPage.getContent();

        if(products.isEmpty())
        {
            throw new APIException("No products present in the category: "+category.getCategoryName());
        }

        List<ProductDTO>productDTOS=products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> productPage=productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%',pageDetails);
//        List<Product> products=productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%'); This was used when pagin was not implemented

        List<Product>products=productPage.getContent();
        if(products.isEmpty())
        {
            throw new APIException("No products found with keyword:"+keyword);
        }

        List<ProductDTO>productDTOS=products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        //Get the existing product from DB
        Product productFromDb=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));

        Product product=modelMapper.map(productDTO,Product.class);

        //Update the product with the info in the RequestBody
        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setSpecialPrice(product.getSpecialPrice());

        //save to db
        Product savedProduct=productRepository.save(productFromDb);

        //This code below is to update this product in the carts (wherever it is added),when this product is updated
        List<Cart>carts=cartRepository.findCartsByProductId(productId);
        List<CartDTO>cartDTOS=carts.stream().map(cart->
        {
            CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
            List<ProductDTO>products=cart.getCartItems().stream()
                    .map(p->modelMapper.map(p.getProduct(),ProductDTO.class)).toList();
            cartDTO.setProducts(products);
            return cartDTO;
        }).toList();

        cartDTOS.forEach(cart->cartService.updateProductInCarts(cart.getCartId(),productId));


        return modelMapper.map(savedProduct,ProductDTO.class);

    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
       Product productToDelete=productRepository.findById(productId)
               .orElseThrow(()->new ResourceNotFoundException("Prodcut","productId",productId));

        //Deleting the product from all the carts
       List<Cart>carts=cartRepository.findCartsByProductId(productId);
       carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(),productId));

       productRepository.delete(productToDelete);
       return modelMapper.map(productToDelete,ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
       //Get the product from DB
        Product productFromDb=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));

        //Upload the image to Server
        //Get the filename of uploaded image
        String fileName= fileService.uploadImage(path, image);

        //updating the new filename to the product
        productFromDb.setImage(fileName);

        //Save updated product to DB
        Product updatedProduct=productRepository.save(productFromDb);

        //return DTO after mapping product to DTO
        return modelMapper.map(updatedProduct,ProductDTO.class);
    }



}
