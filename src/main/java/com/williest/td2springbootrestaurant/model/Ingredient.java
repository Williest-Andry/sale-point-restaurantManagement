package com.williest.td2springbootrestaurant.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
public class Ingredient {
    private Long id;
    private String name;
    private LocalDateTime latestModification = LocalDateTime.now();
    private Unit unit;
    private List<Price> prices = new ArrayList<>();
    private List<StockMovement> stocksMovement = new ArrayList<>();

    public Double getAvalaibleQuantity() {
        if(stocksMovement.isEmpty() || prices.isEmpty()){
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


    public Double getActualPrice() {
        if(this.prices == null || this.prices.isEmpty()){
            return 0.0;
        }
        return prices.stream().max(Comparator.comparing(Price::getBeginDate)).get().getAmount();
    }

    public List<Price> addPrices(List<Price> prices){
        prices.forEach(price -> price.setIngredient(this));
        if (getPrices() == null || getPrices().isEmpty()){
            this.setPrices(prices);
        } else {
            getPrices().addAll(prices);
        }
        return getPrices();
    }

    public List<StockMovement> addStockMovement(List<StockMovement> stockMovements){
        stockMovements.forEach(stock -> stock.setIngredient(this));
        if(getStocksMovement() == null || getStocksMovement().isEmpty()){
            this.setStocksMovement(stockMovements);
        } else{
            getStocksMovement().addAll(stockMovements);
        }
        return getStocksMovement();
    }
}

