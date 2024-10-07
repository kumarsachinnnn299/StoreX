package com.ecommerce.project.repositories;

import com.ecommerce.project.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.email=?1")//1 after ? mark is showing that value is taken from
                                                        //first parameter of the method i.e email
    Cart findCartByEmail(String email);
    @Query("SELECT c FROM Cart c WHERE c.user.email=?1 AND c.id=?2")
    Cart findCartByEmailAndCartId(String emailId, Long cartId);

    @Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p WHERE p.id=?1")
    List<Cart> findCartsByProductId(Long productId);
}
