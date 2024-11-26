package com.ecommerce.project.controllers;

import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.payload.OrderRequestDTO;
import com.ecommerce.project.service.OrderService;
import com.ecommerce.project.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name="Order Controller", description = "This controller will " +
        "have all the endpoints for orders.")
public class OrderController {
    @Autowired
    AuthUtil authUtil;
    @Autowired
    OrderService orderService;
    @PostMapping("/order/users/payments/{paymentMethod}")
    @Operation(summary = "Order a product.")
    public ResponseEntity<OrderDTO>orderProducts(@PathVariable String paymentMethod,
                                                 @RequestBody OrderRequestDTO orderRequestDTO)
    {
        String emailId=authUtil.loggedInEmail();
        OrderDTO order=orderService.placeOrder(
                emailId,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage()
        );

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}

