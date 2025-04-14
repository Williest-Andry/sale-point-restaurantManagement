package com.williest.td2springbootrestaurant.restController.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateDishOrder {
    private String dishName;
    private Integer dishQuantity;
}
