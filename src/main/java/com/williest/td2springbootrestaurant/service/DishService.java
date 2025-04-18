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
        dishSaved.getIngredients().forEach(dishIngredient -> {
            this.priceDAO.findAllByIngredientId(dishIngredient.getIngredient().getId());
            this.stockMovementDAO.findAllByIngredientId(dishIngredient.getIngredient().getId());
        });
        return dishSaved;
    }

    public Dish updateDishIngredients(Long dishId, List<DishIngredient> dishIngredients){
        Dish dish = dishDAO.findById(dishId);
        List<DishIngredient> foundDishIngredient = this.dishIngredientDAO.findDishIngredientByDishId(dishId);
        if(foundDishIngredient.isEmpty() || dishIngredients.isEmpty()){
            throw new RuntimeException("The list of ingredient can't be empty");
        }
        dishIngredients.forEach(dishIngredient -> {
            if(this.ingredientDAO.findByName(dishIngredient.getName()) == null){
                Ingredient ingredient = new Ingredient();
                ingredient.setName(dishIngredient.getName());
                ingredient.setUnit(dishIngredient.getIngredient().getUnit());
                ingredient.setLatestModification(LocalDateTime.now());
                this.ingredientDAO.save(ingredient);
            }
            else if(this.ingredientDAO.findByName(dishIngredient.getName()) != null){
                DishIngredient foundIngredient = foundDishIngredient.stream()
                        .filter(ingredient -> ingredient.getName().equals(dishIngredient.getName())).toList().getFirst();
                foundDishIngredient.remove(foundIngredient);
                foundIngredient.setRequiredQuantity(dishIngredient.getRequiredQuantity());
                foundDishIngredient.add(foundIngredient);
                dishIngredient.setIngredient(this.ingredientDAO.findByName(dishIngredient.getName()));
            }
        });
        this.dishIngredientDAO.saveAll(foundDishIngredient);
        dish.addDishIngredient(dishIngredients);
        return this.save(dish);
    }

    public List<Dish> getBestSales(LocalDateTime startDate, LocalDateTime endDate, int numberOfDishes){
        return dishDAO.findBestSales(startDate, endDate, numberOfDishes);
    }
}
