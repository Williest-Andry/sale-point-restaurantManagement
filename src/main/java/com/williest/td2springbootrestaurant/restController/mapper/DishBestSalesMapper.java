package com.williest.td2springbootrestaurant.restController.mapper;

import com.williest.td2springbootrestaurant.model.Dish;
import com.williest.td2springbootrestaurant.restController.rest.DishBestSale;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DishBestSalesMapper implements Function<Dish, DishBestSale> {
    @Override
    public DishBestSale apply(Dish dish) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public DishBestSale toDishBestSale(DishBestSale dish) {
        DishBestSale dishBestSale = new DishBestSale();
        dishBestSale.setName(dish.getName());
        dishBestSale.setSalesQuantity(dish.getSalesQuantity());
        dishBestSale.setTotalAmount(dish.getTotalAmount());
        return dishBestSale;
    }
}
