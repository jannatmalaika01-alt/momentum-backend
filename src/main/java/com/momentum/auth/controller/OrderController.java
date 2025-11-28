package com.momentum.auth.controller;

import com.momentum.auth.dto.OrderRequest;
import com.momentum.auth.dto.OrderResponse;
import com.momentum.auth.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // Adjust for your frontend URL
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(orderResponse);
    }
    
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderResponse> getOrderByNumber(@PathVariable String orderNumber) {
        OrderResponse order = orderService.getOrderByNumber(orderNumber);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<List<OrderResponse>> getOrdersByEmail(@PathVariable String email) {
        List<OrderResponse> orders = orderService.getOrdersByEmail(email);
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/test")
public ResponseEntity<List<OrderResponse>> getAllOrdersRaw() {
    List<OrderResponse> orders = orderService.getAllOrders();
    return ResponseEntity.ok(orders);
}

    @GetMapping("/phone/{phone}")
    public ResponseEntity<List<OrderResponse>> getOrdersByPhone(@PathVariable String phone) {
        List<OrderResponse> orders = orderService.getOrdersByPhone(phone);
        return ResponseEntity.ok(orders);
    }
}