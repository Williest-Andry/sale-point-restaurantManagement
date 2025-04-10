package com.williest.td2springbootrestaurant.service;

import com.williest.td2springbootrestaurant.model.Dish;
import com.williest.td2springbootrestaurant.repository.DishDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishDAO dishDAO;

    public List<Dish> getBestSales(LocalDateTime startDate, LocalDateTime endDate, int numberOfDishes){
        return dishDAO.findBestSales(startDate, endDate, numberOfDishes);
    }
}
