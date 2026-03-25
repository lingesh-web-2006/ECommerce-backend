package com.ecommerce.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.dto.OrderRequest;
import com.ecommerce.service.OrderService;
@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest request) {
        orderService.placeOrder(request);
        return ResponseEntity.ok("Order saved successfully");
    }
} 