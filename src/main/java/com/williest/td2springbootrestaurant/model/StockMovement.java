package com.williest.td2springbootrestaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class StockMovement {
    private Long id;
    @JsonIgnore
    private Ingredient ingredient;
    private StockMovementType movementType;
    private LocalDateTime moveDate;
    private Double quantity;
    private Unit unit;
}
