package com.williest.td2springbootrestaurant.restController;

import com.williest.td2springbootrestaurant.repository.CustomDataSource;
import com.williest.td2springbootrestaurant.repository.DishDAO;
import com.williest.td2springbootrestaurant.entity.Dish;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DishController {
    DishDAO dishDAO = new DishDAO(new CustomDataSource());

    @GetMapping("/dishes")
    public List<Dish> getAllDishes(){
        return dishDAO.findAll(1,5);
    }

    @GetMapping("/dishes/{id}")
    public Dish getDishById(@PathVariable int id){
        return dishDAO.findById(id, null, null);
    }
}
