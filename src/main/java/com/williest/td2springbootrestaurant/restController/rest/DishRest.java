package com.williest.td2springbootrestaurant.restController.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.williest.td2springbootrestaurant.model.DishIngredient;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@AllArgsConstructor
public class DishRest {
    private Long id;
    private String name;
    private Double unitPrice;
    private List<DishIngredientRest> ingredients = new ArrayList<>();

    public int getAvalaibleQuantity() {
        return ingredients.isEmpty() ? 0 : ingredients.stream()
                .map(ingredient -> ingredient.getAvalaibleQuantity() / ingredient.getRequiredQuantity())
                .min(Comparator.naturalOrder())
                .orElse(0.0)
                .intValue();
    }
}