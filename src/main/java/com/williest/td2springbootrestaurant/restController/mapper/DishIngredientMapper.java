package com.williest.td2springbootrestaurant.restController.mapper;

import com.williest.td2springbootrestaurant.model.DishIngredient;
import com.williest.td2springbootrestaurant.repository.PriceDAO;
import com.williest.td2springbootrestaurant.restController.rest.CreateIngredient;
import com.williest.td2springbootrestaurant.restController.rest.DishIngredientRest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.function.Function;

@Component
public class DishIngredientMapper implements Function<DishIngredient, DishIngredientRest> {
    private PriceDAO priceDAO;

    @Override
    public DishIngredientRest apply(DishIngredient dishIngredient) {
        return new DishIngredientRest(
                dishIngredient.getId(),
                dishIngredient.getName(),
//                "test",
                dishIngredient.getRequiredQuantity(),
                dishIngredient.getIngredient().getPrices(),
                dishIngredient.getIngredient().getStocksMovement()
//                new ArrayList<>(),
//                new ArrayList<>()
        );
    }

    public DishIngredient toModel(CreateIngredient createIngredient){
        DishIngredient dishIngredient = new DishIngredient();
        dishIngredient.setName(createIngredient.getName());
        dishIngredient.setRequiredQuantity(createIngredient.getRequiredQuantity());
        return dishIngredient;
    }
}
