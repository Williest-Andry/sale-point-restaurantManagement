package com.williest.td2springbootrestaurant.service;

import com.williest.td2springbootrestaurant.model.Dish;
import com.williest.td2springbootrestaurant.model.DishIngredient;
import com.williest.td2springbootrestaurant.model.Ingredient;
import com.williest.td2springbootrestaurant.repository.*;
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
    private final PriceDAO priceDAO;
    private final StockMovementDAO stockMovementDAO;

    public List<Dish> getAllDishes(){
        List<Dish> dishes = dishDAO.findAll(1, 10);
        dishes.forEach(dish -> {
            dish.setIngredients(this.dishIngredientDAO.findDishIngredientByDishId(dish.getId()));

            dish.getIngredients().forEach(dishIngredient -> {
                Long ingredientId = dishIngredient.getIngredient().getId();
                dishIngredient.getIngredient().setPrices(this.priceDAO.findAllByIngredientId(ingredientId));
                dishIngredient.getIngredient().setStocksMovement(this.stockMovementDAO.findAllByIngredientId(ingredientId));
            });
        });
        return dishes;
    }

    public Dish save(Dish dish) {
        Dish dishSaved = dishDAO.save(dish);
        dishSaved.setIngredients(this.dishIngredientDAO.saveAll(dish.getIngredients()));
        return dishSaved;
    }

    public Dish updateDishIngredients(Long dishId, List<DishIngredient> dishIngredients){
        Dish dish = dishDAO.findById(dishId);
        dishIngredients.forEach(dishIngredient -> {
            if(this.ingredientDAO.findByName(dishIngredient.getName()) == null){
                throw new RuntimeException(dishIngredient.getIngredient().getName()+" not exists");
            }
            dishIngredient.setIngredient(this.ingredientDAO.findByName(dishIngredient.getName()));
        });
        dish.addDishIngredient(dishIngredients);
        return this.save(dish);
    }

    public List<Dish> getBestSales(LocalDateTime startDate, LocalDateTime endDate, int numberOfDishes){
        return dishDAO.findBestSales(startDate, endDate, numberOfDishes);
    }
}
