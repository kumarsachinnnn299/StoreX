package com.ecommerce.project.repositories;

import com.ecommerce.project.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id=?2 AND ci.product.id=?1")
    CartItem findCartItemByProductIdAndCartId(Long productId, Long cartId);
    @Modifying//This tells the JPA that we are modifyingh the DB
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id=?1 AND ci.product.id=?2")
    void deleteCartItemByProductIdAndCartId(Long cartId, Long productId);
}
