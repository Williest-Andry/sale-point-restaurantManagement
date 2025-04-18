package com.williest.td2springbootrestaurant.restController;

import com.williest.td2springbootrestaurant.model.DishIngredient;
import com.williest.td2springbootrestaurant.model.Ingredient;
import com.williest.td2springbootrestaurant.repository.DataSourceDB;
import com.williest.td2springbootrestaurant.repository.DishDAO;
import com.williest.td2springbootrestaurant.model.Dish;
import com.williest.td2springbootrestaurant.restController.mapper.DishBestSalesMapper;
import com.williest.td2springbootrestaurant.restController.mapper.DishIngredientMapper;
import com.williest.td2springbootrestaurant.restController.mapper.DishRestMapper;
import com.williest.td2springbootrestaurant.restController.mapper.IngredientRestMapper;
import com.williest.td2springbootrestaurant.restController.rest.CreateIngredient;
import com.williest.td2springbootrestaurant.restController.rest.DishBestSale;
import com.williest.td2springbootrestaurant.restController.rest.DishRest;
import com.williest.td2springbootrestaurant.service.DishService;
import com.williest.td2springbootrestaurant.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final DishBestSalesMapper dishBestSalesMapper;

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
            System.out.println("Dish = " + dish);
            System.out.println("Mapped = " + dishRestMapper.apply(dish));
            return ResponseEntity.ok().body(dishRestMapper.apply(dish));
        } catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

//    @GetMapping("/bestSales")
//    public ResponseEntity<Object> getBestSales(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate,
//                                   @RequestParam int numberOfDishes){
//        List<Dish> dishes = this.dishService.getBestSales(startDate, endDate, numberOfDishes);
//        List<DishBestSale> dishBestSales = dishes.stream().map(bestDish -> {
//            DishBestSale dish = (DishBestSale) bestDish;
//            dish.setIngredients(new ArrayList<>());
//            dish.setUnitPrice(0.0);
//            return dish;
//        }).toList();
//        List<DishBestSale> bestDishes = dishBestSales.stream().map(this.dishBestSalesMapper::toDishBestSale).toList();
//        return ResponseEntity.ok().body(bestDishes);
//    }
}
