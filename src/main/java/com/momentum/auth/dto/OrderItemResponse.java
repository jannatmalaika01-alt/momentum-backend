package com.momentum.auth.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemResponse {
    private String productName;
    private String size;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}