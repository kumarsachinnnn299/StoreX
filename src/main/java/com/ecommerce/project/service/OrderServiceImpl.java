package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.*;
import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.payload.OrderItemDTO;
import com.ecommerce.project.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    CartService cartService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName,
                               String pgPaymentId, String pgStatus, String pgResponseMessage) {
        //We will convert cart to order i.e cartItems to OrderItems
        Cart cart=cartRepository.findCartByEmail(emailId);
        if(cart==null)
        {
            throw new ResourceNotFoundException("Cart", "email",emailId);
        }

        Address address=addressRepository.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));

        //Setting the order
        Order order=new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted !");
        order.setAddress(address);

        //Setting the order's payment
        Payment payment=new Payment(paymentMethod,pgPaymentId,pgStatus,pgResponseMessage,pgName);
        payment.setOrder(order);
        payment=paymentRepository.save(payment);
        order.setPayment(payment);

        //Saving the order
        Order savedOrder=orderRepository.save(order);

        //Converting CartItems to OrderItems
        List<CartItem> cartItems=cart.getCartItems();
        if(cartItems.isEmpty()){
            throw new APIException("Cart is Empty!");
        }
        List<OrderItem>orderItems=new ArrayList<>();
        for(CartItem cartItem:cartItems)
        {
            OrderItem orderItem=new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        //Saving orderItems
        orderItems=orderItemRepository.saveAll(orderItems);

        //Updating the stocks of products and removing items from cart
        cart.getCartItems().forEach(item->
        {
            int quantity=item.getQuantity();
            Product product=item.getProduct();

            //Reduce Stock quantity
            product.setQuantity(product.getQuantity()-quantity);

            //save product back to db
            productRepository.save(product);

            //removing items from the cart
            cartService.deleteProductFromCart(cart.getCartId(),item.getProduct().getProductId());
        });




        OrderDTO orderDTO=modelMapper.map(savedOrder,OrderDTO.class);

        //Adding OrderItemDTO in OrderDTO
        orderItems.forEach(item->orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));
        orderDTO.setAddressId(addressId);

        return  orderDTO;
    }
}
