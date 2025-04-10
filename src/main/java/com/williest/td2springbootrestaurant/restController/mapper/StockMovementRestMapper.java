package com.williest.td2springbootrestaurant.restController.mapper;

import com.williest.td2springbootrestaurant.model.StockMovement;
import com.williest.td2springbootrestaurant.restController.rest.CreateStockMovement;
import com.williest.td2springbootrestaurant.restController.rest.StockMovementRest;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class StockMovementRestMapper implements Function<StockMovement, StockMovementRest> {
    @Override
    public StockMovementRest apply(StockMovement stockMovement) {
        return new StockMovementRest(
                stockMovement.getId(),
                stockMovement.getMovementType(),
                stockMovement.getMoveDate(),
                stockMovement.getQuantity(),
                stockMovement.getUnit()
        );
    }

    public StockMovement toModel(CreateStockMovement createStockMovement){
        StockMovement stockMovement = new StockMovement();
        stockMovement.setMovementType(createStockMovement.getMovementType());
        stockMovement.setMoveDate(createStockMovement.getMoveDate());
        stockMovement.setQuantity(createStockMovement.getQuantity());
        stockMovement.setUnit(createStockMovement.getUnit());
        return stockMovement;
    }
}
