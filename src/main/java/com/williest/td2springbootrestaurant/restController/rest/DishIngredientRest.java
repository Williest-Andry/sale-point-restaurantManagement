package com.williest.td2springbootrestaurant.restController.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.williest.td2springbootrestaurant.model.Price;
import com.williest.td2springbootrestaurant.model.StockMovement;
import com.williest.td2springbootrestaurant.model.StockMovementType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@AllArgsConstructor
@ToString
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

    public StockMovement getActualStockMovement() {
        return stocksMovement.stream().max(Comparator.comparing(StockMovement::getMoveDate)).get();
    }

    public Double getAvalaibleQuantity() {
        if(stocksMovement.isEmpty()){
            return 0.0;
        }
        Double totalStockIn = stocksMovement.stream()
                .filter(stockMovement -> stockMovement.getMovementType() == StockMovementType.IN)
                .map(StockMovement::getQuantity)
                .reduce(Double::sum)
                .orElse(0.0);

        Double totalStockOut = stocksMovement.stream()
                .filter(stockMovement -> stockMovement.getMovementType() == StockMovementType.OUT)
                .map(StockMovement::getQuantity)
                .reduce(Double::sum)
                .orElse(0.0);

        return totalStockIn - totalStockOut;
    }
}
