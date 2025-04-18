package com.williest.td2springbootrestaurant.service;

import com.williest.td2springbootrestaurant.model.DishOrderStatus;
import com.williest.td2springbootrestaurant.repository.DishOrderStatusDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishOrderStatusService {
    private final DishOrderStatusDAO dishOrderStatusDAO;

    public List<DishOrderStatus> getAllByDishOrderId(Long dishOrderId) {
        return this.dishOrderStatusDAO.findAllByDishOrderId(dishOrderId);
    }
}
