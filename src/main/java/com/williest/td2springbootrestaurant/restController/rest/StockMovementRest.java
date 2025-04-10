package com.williest.td2springbootrestaurant.restController.rest;

import com.williest.td2springbootrestaurant.model.StockMovementType;
import com.williest.td2springbootrestaurant.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class StockMovementRest {
    private Long id;
    private StockMovementType movementType;
    private LocalDateTime moveDate;
    private Double quantity;
    private Unit unit;
}
