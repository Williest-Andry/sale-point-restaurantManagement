package com.williest.td2springbootrestaurant.restController.mapper;

import com.williest.td2springbootrestaurant.model.Ingredient;
import com.williest.td2springbootrestaurant.restController.rest.CreateIngredient;
import com.williest.td2springbootrestaurant.restController.rest.IngredientRest;
import com.williest.td2springbootrestaurant.restController.rest.PriceRest;
import com.williest.td2springbootrestaurant.restController.rest.StockMovementRest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class IngredientRestMapper implements Function<Ingredient, IngredientRest> {
    private final PriceRestMapper priceRestMapper;
    private final StockMovementRestMapper stockMovementRestMapper;

    @Override
    public IngredientRest apply(Ingredient ingredient) {
        List<PriceRest> pricesRest = ingredient.getPrices().stream().map(priceRestMapper::apply).toList();
        List<StockMovementRest> stockMovementsRest = ingredient.getStocksMovement().stream()
                .map(stockMovementRestMapper::apply).toList();
        return new IngredientRest(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getLatestModification(),
                ingredient.getUnit(),
                pricesRest,
                stockMovementsRest
        );
    }

    public Ingredient toModel(CreateIngredient createIngredient){
        return new Ingredient();
    }
}
