package com.williest.td2springbootrestaurant.restController;

import com.williest.td2springbootrestaurant.repository.DataSourceDB;
import com.williest.td2springbootrestaurant.repository.DishDAO;
import com.williest.td2springbootrestaurant.model.Dish;
import com.williest.td2springbootrestaurant.restController.mapper.DishRestMapper;
import com.williest.td2springbootrestaurant.restController.rest.DishRest;
import com.williest.td2springbootrestaurant.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DishController {
    private final DishDAO dishDAO;
    private final DishService dishService;
    private final DishRestMapper dishRestMapper;

    @GetMapping("/dishes")
    public ResponseEntity<Object> getAllDishes(){
        try{
            List<Dish> dishes = dishService.getAllDishes();
            System.out.println(dishes.getFirst().getIngredients());
            List<DishRest> dishesRest = dishes.stream().map(dishRestMapper::apply).toList();
            return ResponseEntity.ok().body(dishesRest);
        } catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @GetMapping("/dishes/{id}")
    public Dish getDishById(@PathVariable int id){
        return null;
    }

    @GetMapping("/bestSales")
    public List<Dish> getBestSales(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate,
                                   @RequestParam int numberOfDishes){
        // WHERE order_status='FINISHED' AND range of date
        return null;
    }
}
