package com.momentum.auth.service;

import com.momentum.auth.dto.OrderRequest;
import com.momentum.auth.dto.OrderResponse;
import com.momentum.auth.entity.Order;
import com.momentum.auth.entity.OrderItem;
import com.momentum.auth.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.momentum.auth.dto.OrderItemRequest;  // Add this import
import com.momentum.auth.dto.OrderItemResponse; // Add this import
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setFirstName(orderRequest.getFirstName());
        order.setLastName(orderRequest.getLastName());
        order.setEmail(orderRequest.getEmail());
        order.setPhone(orderRequest.getPhone());
        order.setAddress(orderRequest.getAddress());
        order.setCity(orderRequest.getCity());
        order.setZipCode(orderRequest.getZipCode());
        
        // Calculate totals
        BigDecimal totalAmount = calculateTotalAmount(orderRequest.getItems());
        BigDecimal codFee = new BigDecimal("50.00");
        BigDecimal finalAmount = totalAmount.add(codFee);
        
        order.setTotalAmount(totalAmount);
        order.setCodFee(codFee);
        order.setFinalAmount(finalAmount);
        
        // Add order items
        orderRequest.getItems().forEach(itemRequest -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductName(itemRequest.getProductName());
            orderItem.setSize(itemRequest.getSize());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(itemRequest.getUnitPrice());
            orderItem.calculateSubtotal();
            order.getOrderItems().add(orderItem);
        });
        
        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }
    
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllOrdersByDateDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapToResponse(order);
    }
    
    public OrderResponse getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapToResponse(order);
    }
    
    public List<OrderResponse> getOrdersByEmail(String email) {
        return orderRepository.findByEmailOrderByOrderDateDesc(email)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public List<OrderResponse> getOrdersByPhone(String phone) {
        return orderRepository.findByPhoneOrderByOrderDateDesc(phone)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private BigDecimal calculateTotalAmount(List<OrderItemRequest> items) {
        return items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setFirstName(order.getFirstName());
        response.setLastName(order.getLastName());
        response.setEmail(order.getEmail());
        response.setPhone(order.getPhone());
        response.setAddress(order.getAddress());
        response.setCity(order.getCity());
        response.setZipCode(order.getZipCode());
        response.setTotalAmount(order.getTotalAmount());
        response.setCodFee(order.getCodFee());
        response.setFinalAmount(order.getFinalAmount());
        response.setOrderStatus(order.getOrderStatus().name());
        response.setPaymentMethod(order.getPaymentMethod().name());
        response.setPaymentStatus(order.getPaymentStatus().name());
        
        response.setItems(order.getOrderItems().stream().map(item -> {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setProductName(item.getProductName());
            itemResponse.setSize(item.getSize());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setUnitPrice(item.getUnitPrice());
            itemResponse.setSubtotal(item.getSubtotal());
            return itemResponse;
        }).collect(Collectors.toList()));
        
        return response;
    }
}