package com.williest.td2springbootrestaurant.restController.rest;

import com.williest.td2springbootrestaurant.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
}
