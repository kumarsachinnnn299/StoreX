package com.ecommerce.project.controllers;

import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.service.CartService;
import com.ecommerce.project.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name="Cart Controller", description = "This controller will " +
        "have all the endpoints for cart.")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    AuthUtil authUtil;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    @Operation(summary = "Add a product to a user's cart.")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity){
        CartDTO cartDTO=cartService.addProductToCart(productId,quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    //This methods is to get all the carts of all the users
    @GetMapping("/carts")
    @Operation(summary = "Get all carts.")
    public ResponseEntity<List<CartDTO>>getCarts(){
        List<CartDTO>cartDTOS=cartService.getAllCarts();
        return new ResponseEntity<>(cartDTOS,HttpStatus.FOUND);
    }

    //This methods is for getting a users' cart
    @GetMapping("/carts/users/cart")
    @Operation(summary = "Get a user's cart.")
    public ResponseEntity<CartDTO>getCart(){
        String emailId= authUtil.loggedInEmail();
        Long cartId=cartRepository.findCartByEmail(emailId).getCartId();

     CartDTO cartDTO=  cartService.getCart(emailId, cartId);

     /*
     here in this above methos: getCart(),  although email id is sufficient to get the users cart but cart id
     is also given for the case in future when we want to scale the app and want to
     have multiple carts for a single user: one for desktop and one for mobile
      */
     return new ResponseEntity<>(cartDTO,HttpStatus.OK);
    }

    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    @Operation(summary = "Update a product quantity in cart.")
    public ResponseEntity<CartDTO>updateCartProduct(@PathVariable Long productId,
                                                    @PathVariable String operation
    ){
        CartDTO cartDTO=cartService.updateProductQuantityInCart(productId,
                operation.equalsIgnoreCase("delete")?-1:1);
        return new ResponseEntity<>(cartDTO,HttpStatus.OK);
    }

    @DeleteMapping("/carts/{cartId}/product/{productId}")
    @Operation(summary = "Delete a product from cart.")
    public ResponseEntity<String>deleteProductFromCart(@PathVariable Long cartId,
                                                       @PathVariable Long productId)
    {
        String status=cartService.deleteProductFromCart(cartId,productId);
        return new ResponseEntity<>(status,HttpStatus.OK);
    }
}
