package com.ecommerce.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;
    @ManyToOne//one product can be in multiple orderItems of different different orders
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne//one order can have many order items
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer quantity;
    private Double discount;
    private  Double orderedProductPrice;
}
