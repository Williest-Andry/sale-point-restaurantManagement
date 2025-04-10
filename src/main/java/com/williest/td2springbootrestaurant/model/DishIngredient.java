package com.williest.td2springbootrestaurant.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DishIngredient {
    private Long id;
    private String name;
    private Double requiredQuantity;
    private Ingredient ingredient;
    private Dish dish;
}
