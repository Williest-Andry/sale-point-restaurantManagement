package com.williest.td2springbootrestaurant.restController.rest;

import com.williest.td2springbootrestaurant.model.Price;
import com.williest.td2springbootrestaurant.model.StockMovement;
import com.williest.td2springbootrestaurant.model.StockMovementType;
import com.williest.td2springbootrestaurant.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Getter
public class IngredientRest {
    private Long id;
    private String name;
    private LocalDateTime latestModification = LocalDateTime.now();
    private Unit unit;
    private List<PriceRest> prices = new ArrayList<>();
    private List<StockMovementRest> stocksMovement = new ArrayList<>();

    public Double getActualPrice() {
        if(this.prices == null || this.prices.isEmpty()){
            return 0.0;
        }
        return prices.stream().max(Comparator.comparing(PriceRest::getBeginDate)).get().getAmount();
    }

    public Double getAvalaibleQuantity() {
        if(stocksMovement.isEmpty() || prices.isEmpty()){
            return 0.0;
        }
        Double totalStockIn = stocksMovement.stream()
                .filter(stockMovement -> stockMovement.getMovementType() == StockMovementType.IN)
                .map(StockMovementRest::getQuantity)
                .reduce(Double::sum)
                .orElse(0.0);

        Double totalStockOut = stocksMovement.stream()
                .filter(stockMovement -> stockMovement.getMovementType() == StockMovementType.OUT)
                .map(StockMovementRest::getQuantity)
                .reduce(Double::sum)
                .orElse(0.0);

        return totalStockIn - totalStockOut;
    }
}
