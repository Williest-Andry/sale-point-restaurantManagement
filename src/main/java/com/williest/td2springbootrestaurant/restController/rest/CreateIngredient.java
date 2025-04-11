package com.williest.td2springbootrestaurant.restController.rest;

import com.williest.td2springbootrestaurant.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateIngredient {
    private String name;
    private Unit unit;
    private Double requiredQuantity;
}
