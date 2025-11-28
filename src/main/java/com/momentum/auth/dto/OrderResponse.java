package com.momentum.auth.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String zipCode;
    private BigDecimal totalAmount;
    private BigDecimal codFee;
    private BigDecimal finalAmount;
    private String orderStatus;
    private String paymentMethod;
    private String paymentStatus;
    private List<OrderItemResponse> items;
}