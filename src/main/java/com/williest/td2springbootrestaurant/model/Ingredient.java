package com.williest.td2springbootrestaurant.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Ingredient {
    private Long id;
    private String name;
    private LocalDateTime latestModification;
    private Double unitPrice = this.getActualPrice();
    private Unit unit;
    private List<Price> prices = new ArrayList<>();
    private List<StockMovement> stocksMovement = new ArrayList<>();

    @JsonIgnore
    public Double getAvalaibleQuantity() {
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
}

