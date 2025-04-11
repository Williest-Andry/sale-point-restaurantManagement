package com.williest.td2springbootrestaurant.service;

import com.williest.td2springbootrestaurant.model.Order;
import com.williest.td2springbootrestaurant.repository.OrderDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderDAO orderDAO;

    public List<Order> getAllOrders(){
        return orderDAO.findAll();
    }

    public Order getOrderByReference(Long reference){
        return orderDAO.findByReference(reference);
    }
}
