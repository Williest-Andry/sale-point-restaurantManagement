package com.williest.td2springbootrestaurant.restController.mapper;

import com.williest.td2springbootrestaurant.model.DishOrder;
import com.williest.td2springbootrestaurant.restController.rest.DishOrderRest;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DishOrderRestMapper implements Function<DishOrder, DishOrderRest> {
    @Override
    public DishOrderRest apply(DishOrder dishOrder) {
        return  new DishOrderRest(
                dishOrder.getDishQuantity(),
                dishOrder.getDishOrderStatus()
        );
    }
}
