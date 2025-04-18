package com.williest.td2springbootrestaurant.restController.rest;

import com.williest.td2springbootrestaurant.model.Dish;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DishBestSale extends Dish {
    private String name;
    private int salesQuantity;
    private Double totalAmount;
}
