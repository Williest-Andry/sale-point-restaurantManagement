package com.williest.td2springbootrestaurant.restController;

import com.williest.td2springbootrestaurant.model.DishIngredient;
import com.williest.td2springbootrestaurant.model.Ingredient;
import com.williest.td2springbootrestaurant.repository.DataSourceDB;
import com.williest.td2springbootrestaurant.repository.DishDAO;
import com.williest.td2springbootrestaurant.model.Dish;
import com.williest.td2springbootrestaurant.restController.mapper.DishIngredientMapper;
import com.williest.td2springbootrestaurant.restController.mapper.DishRestMapper;
import com.williest.td2springbootrestaurant.restController.mapper.IngredientRestMapper;
import com.williest.td2springbootrestaurant.restController.rest.CreateIngredient;
import com.williest.td2springbootrestaurant.restController.rest.DishRest;
import com.williest.td2springbootrestaurant.service.DishService;
import com.williest.td2springbootrestaurant.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DishController {
    private final DishDAO dishDAO;
    private final DishService dishService;
    private final DishRestMapper dishRestMapper;
    private final IngredientRestMapper ingredientRestMapper;
    private final IngredientService ingredientService;
    private final DishIngredientMapper dishIngredientMapper;

    @GetMapping("/dishes")
    public ResponseEntity<Object> getAllDishes(){
        try{
            List<Dish> dishes = dishService.getAllDishes();
            List<DishRest> dishesRest = dishes.stream().map(dishRestMapper::apply).toList();
            return ResponseEntity.ok().body(dishesRest);
        } catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PutMapping("/dishes/{dishId}/ingredients")
    public ResponseEntity<Object> getDishById(@PathVariable Long dishId,
                                              @RequestBody List<CreateIngredient> ingredientsToCreate){
        try{
            List<DishIngredient> ingredients = ingredientsToCreate.stream().map(dishIngredientMapper::toModel).toList();
            Dish dish = dishService.updateDishIngredients(dishId, ingredients);
            return ResponseEntity.ok().body(dishRestMapper.apply(dish));
        } catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @GetMapping("/bestSales")
    public List<Dish> getBestSales(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate,
                                   @RequestParam int numberOfDishes){
        // WHERE order_status='FINISHED' AND range of date
        throw new UnsupportedOperationException("not implemented");
    }
}
