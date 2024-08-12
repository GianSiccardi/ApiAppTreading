package com.giansiccardi.dto;

import com.giansiccardi.enums.OrderType;
import lombok.Data;

@Data
public class CreateOrderRequest {

private String coinId;
private double quantity;
private OrderType orderType;
}
