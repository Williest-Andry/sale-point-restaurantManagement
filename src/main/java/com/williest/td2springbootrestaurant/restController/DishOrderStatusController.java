package com.williest.td2springbootrestaurant.restController;

import com.williest.td2springbootrestaurant.service.DishOrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DishOrderStatusController {
    private final DishOrderStatusService dishOrderStatusService;

    @GetMapping("/dishOrderStatus/{dishId}")
    public ResponseEntity<Object> getDishOrderStatusByDishOrderId(@PathVariable Long dishId) {
        return ResponseEntity.ok().body(this.dishOrderStatusService.getAllByDishOrderId(dishId));
    }
}
