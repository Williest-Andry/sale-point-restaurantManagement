package com.williest.td2springbootrestaurant.restController.mapper;

import com.williest.td2springbootrestaurant.model.Dish;
import com.williest.td2springbootrestaurant.model.DishOrder;
import com.williest.td2springbootrestaurant.restController.rest.CreateDishOrder;
import com.williest.td2springbootrestaurant.restController.rest.DishOrderRest;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DishOrderRestMapper implements Function<DishOrder, DishOrderRest> {
    @Override
    public DishOrderRest apply(DishOrder dishOrder) {
        return  new DishOrderRest(
                dishOrder.getDish().getName(),
                dishOrder.getDish().getUnitPrice(),
                dishOrder.getDishQuantity(),
                dishOrder.getDishOrderStatus()
        );
    }

    public DishOrder toModel(CreateDishOrder createDishOrder){
        DishOrder dishOrder = new DishOrder();
        dishOrder.setDishQuantity(createDishOrder.getDishQuantity());
        Dish dish = new Dish();
        dish.setName(createDishOrder.getDishName());
        dishOrder.setDish(dish);
        return dishOrder;
    }
}
