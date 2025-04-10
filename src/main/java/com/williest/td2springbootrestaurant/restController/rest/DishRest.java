package com.williest.td2springbootrestaurant.restController.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class DishRest {
    private Long id;
    private String name;
    private Double unitPrice;
    private List<DishIngredientRest> ingredients = new ArrayList<>();
}
