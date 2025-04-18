package com.williest.td2springbootrestaurant.restController.mapper;

import com.williest.td2springbootrestaurant.model.Dish;
import com.williest.td2springbootrestaurant.restController.rest.DishIngredientRest;
import com.williest.td2springbootrestaurant.restController.rest.DishRest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishRestMapper implements Function<Dish, DishRest> {
    private final DishIngredientMapper dishIngredientMapper;

    @Override
    public DishRest apply(Dish dish) {
        List<DishIngredientRest> dishIngredientsRest = dish.getIngredients().stream().map(dishIngredientMapper::apply).toList();
        DishRest dishRest = new DishRest(
                dish.getId(),
                dish.getName(),
                dish.getUnitPrice(),
                dishIngredientsRest
        );
        return dishRest;
    }
}
