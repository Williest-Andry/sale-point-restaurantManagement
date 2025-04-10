package com.williest.td2springbootrestaurant.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
public class Dish {
    private Long id;
    private String name;
    private Double unitPrice;
    private List<DishIngredient> ingredients = new ArrayList<>();

    public int getAvalaibleQuantity() {
        return ingredients.stream()
                .map(ingredient -> ingredient.getIngredient().getAvalaibleQuantity() / ingredient.getRequiredQuantity())
                .max(Comparator.naturalOrder())
                .orElse(0.0)
                .intValue();
    }

    public double getIngredientCost() {
        return ingredients.stream()
                .map(ingredient -> ingredient.getIngredient().getActualPrice())
                .reduce(0.0, Double::sum);
    }

    public double getGrossMargin() {
        return unitPrice - this.getIngredientCost();
    }
}