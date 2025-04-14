package com.williest.td2springbootrestaurant.restController.rest;

import com.williest.td2springbootrestaurant.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class CreateOrder {
    private OrderStatus orderStatus;
    private List<CreateDishOrder> dishOrders = new ArrayList<>();
}
