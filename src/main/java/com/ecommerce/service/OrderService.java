 package com.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.OrderRequest;
import com.ecommerce.entity.Order;
import com.ecommerce.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void placeOrder(OrderRequest request) {

        Order order = new Order();
 
        order.setName(request.getName());
        order.setAddress(request.getAddress());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        order.setNotes(request.getNotes());

        orderRepository.save(order); 
    }
}