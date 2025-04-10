package com.williest.td2springbootrestaurant.restController.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.williest.td2springbootrestaurant.model.Price;
import com.williest.td2springbootrestaurant.model.StockMovement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class DishIngredientRest {
    private final Long id;
    private final String name;
    private final Double requiredQuantity;
    @JsonIgnore
    private List<Price> prices = new ArrayList<>();
    @JsonIgnore
    private List<StockMovement> stocksMovement = new ArrayList<>();

    public Double getActualPrice() {
        if(this.prices == null || this.prices.isEmpty()){
            return 0.0;
        }
        return prices.stream().max(Comparator.comparing(Price::getBeginDate)).get().getAmount();
    }

//    public StockMovement getActualStockMovement() {
//
//    }
}
