package com.williest.td2springbootrestaurant.restController;

import com.williest.td2springbootrestaurant.repository.DataSourceDB;
import com.williest.td2springbootrestaurant.repository.DishDAO;
import com.williest.td2springbootrestaurant.model.Dish;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class DishController {
    DishDAO dishDAO = new DishDAO(new DataSourceDB());

    @GetMapping("/dishes")
    public List<Dish> getAllDishes(){
        return dishDAO.findAll(1,5);
    }

    @GetMapping("/dishes/{id}")
    public Dish getDishById(@PathVariable int id){
        return dishDAO.findById(id, null, null);
    }

    @GetMapping("/bestSales")
    public List<Dish> getBestSales(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate,
                                   @RequestParam int numberOfDishes){
        // WHERE order_status='FINISHED' AND range of date

    }
}
