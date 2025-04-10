package com.williest.td2springbootrestaurant.restController.mapper;

import com.williest.td2springbootrestaurant.model.Price;
import com.williest.td2springbootrestaurant.restController.rest.CreateIngredientPrice;
import com.williest.td2springbootrestaurant.restController.rest.PriceRest;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PriceRestMapper implements Function<Price, PriceRest> {
    @Override
    public PriceRest apply(Price price) {
        return new PriceRest(
                price.getId(),
                price.getAmount(),
                price.getBeginDate()
        );
    }

    public Price toModel(CreateIngredientPrice createIngredientPrice){
        Price price = new Price();
        price.setAmount(createIngredientPrice.getAmount());
        price.setBeginDate(createIngredientPrice.getBeginDate());
        return price;
    }
}
