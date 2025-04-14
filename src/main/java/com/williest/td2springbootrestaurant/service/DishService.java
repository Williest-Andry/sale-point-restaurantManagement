package com.williest.td2springbootrestaurant.service;

import com.williest.td2springbootrestaurant.model.Dish;
import com.williest.td2springbootrestaurant.model.DishIngredient;
import com.williest.td2springbootrestaurant.repository.DishDAO;
import com.williest.td2springbootrestaurant.repository.DishIngredientDAO;
import com.williest.td2springbootrestaurant.repository.IngredientDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishDAO dishDAO;
    private final DishIngredientDAO dishIngredientDAO;
    private final IngredientDAO ingredientDAO;

    public List<Dish> getAllDishes(){
        List<Dish> dishes = dishDAO.findAll(1, 10);
        List<DishIngredient> dishIngredients = new ArrayList<>();
        dishes.forEach(dish -> {
            dish.setIngredients(dishIngredientDAO.findDishIngredientByDishId(dish.getId()));
        });
        return dishes;
    }

    public List<Dish> getBestSales(LocalDateTime startDate, LocalDateTime endDate, int numberOfDishes){
        return dishDAO.findBestSales(startDate, endDate, numberOfDishes);
    }
}
