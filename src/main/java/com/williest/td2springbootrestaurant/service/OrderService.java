package com.williest.td2springbootrestaurant.service;

import com.williest.td2springbootrestaurant.model.*;
import com.williest.td2springbootrestaurant.repository.DishOrderDAO;
import com.williest.td2springbootrestaurant.repository.DishOrderStatusDAO;
import com.williest.td2springbootrestaurant.repository.OrderDAO;
import com.williest.td2springbootrestaurant.repository.OrderStatusDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderDAO orderDAO;
    private final OrderStatusDAO orderStatusDAO;
    private final DishOrderDAO dishOrderDAO;
    private final DishOrderStatusDAO dishOrderStatusDAO;

    public List<Order> getAllOrders(){
        return orderDAO.findAll();
    }

    public Order getOrderByReference(Long reference){
        if(reference.getClass() != Long.class){
            throw new RuntimeException("The reference must be a number!");
        }
        Order order = orderDAO.findByReference(reference);
        order.setDishOrders(this.dishOrderDAO.findAllByOrderId(order.getId()));
        order.setOrderStatus(this.orderStatusDAO.findAllByOrderId(order.getId()));
        return order;
    }

    public Order saveOrder(Long reference, Order order){
        Order foundOrder = orderDAO.findByReference(reference);
        foundOrder.getDishOrders().clear();
        foundOrder.setDishOrders(order.getDishOrders());
        foundOrder.setOrderStatus(order.getOrderStatus());
        Order savedOrder = orderDAO.save(foundOrder);
        this.dishOrderDAO.saveAll(savedOrder.getDishOrders());
//        this.orderStatusDAO.saveAll(savedOrder.getOrderStatus());
        return savedOrder;
    }

    public Order updateOrderDishStatusById(Long reference, Long dishId, Status status){
        Order order = orderDAO.findByReference(reference);
        DishOrder dishOrderToModify = order.getDishOrders().stream().filter(dishOrder -> dishOrder.getDish().getId() == dishId).toList().getFirst();
        order.getDishOrders().remove(dishOrderToModify);
        DishOrderStatus dishOrderStatus = new DishOrderStatus();
        dishOrderStatus.setDishOrder(dishOrderToModify);
        dishOrderStatus.setStatusDate(LocalDateTime.now());
        dishOrderStatus.setStatus(status);
        dishOrderToModify.addAndUpdateStatus(dishOrderStatus);
        order.getDishOrders().add(dishOrderToModify);
        Order savedOrder = orderDAO.save(order);
        savedOrder.getDishOrders().forEach(dishOrder -> this.dishOrderStatusDAO.saveAll(dishOrder.getDishOrderStatus()));
        return savedOrder;
    }
}
