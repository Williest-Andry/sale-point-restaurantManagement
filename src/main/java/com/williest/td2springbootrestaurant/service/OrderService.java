package com.williest.td2springbootrestaurant.service;

import com.williest.td2springbootrestaurant.model.*;
import com.williest.td2springbootrestaurant.repository.*;
import com.williest.td2springbootrestaurant.service.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderDAO orderDAO;
    private final OrderStatusDAO orderStatusDAO;
    private final DishOrderDAO dishOrderDAO;
    private final DishOrderStatusDAO dishOrderStatusDAO;
    private final DishDAO dishDAO;

    public List<Order> getAllOrders(){
        return orderDAO.findAll();
    }

    public Order getOrderByReference(Long reference){
        Order order = orderDAO.findByReference(reference);
        order.setDishOrders(this.dishOrderDAO.findAllByOrderId(order.getId()));
        order.getDishOrders().forEach(dishOrder -> {
            dishOrder.setDishOrderStatus(this.dishOrderStatusDAO.findAllByDishOrderId(dishOrder.getId()));
        });
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
        order.setDishOrders(this.dishOrderDAO.findAllByOrderId(order.getId()));
        DishOrder dishOrderToModify = order.getDishOrders().stream().filter(dishOrder -> dishOrder.getDish().getId().equals(dishId)).toList().getFirst();
        order.getDishOrders().remove(dishOrderToModify);

        DishOrderStatus dishOrderStatus = new DishOrderStatus();
        dishOrderStatus.setDishOrder(dishOrderToModify);
        dishOrderStatus.setStatusDate(LocalDateTime.now());
        dishOrderStatus.setStatus(status);
        dishOrderToModify.addAndUpdateStatus(dishOrderStatus);
        order.getDishOrders().add(dishOrderToModify);
        System.out.println(dishOrderToModify.getDishOrderStatus());

        Order savedOrder = orderDAO.save(order);
        savedOrder.setDishOrders(this.dishOrderDAO.saveAll(order.getDishOrders()));

        System.out.println(savedOrder.getDishOrders().size());
        savedOrder.getDishOrders().forEach(dishOrder -> {
            System.out.println("mety:");
            System.out.println(dishOrder.getDishOrderStatus());
            this.dishOrderStatusDAO.findAllByDishOrderId(dishOrder.getId());
            this.dishOrderStatusDAO.saveAll(dishOrder.getDishOrderStatus());
        });
        savedOrder.getDishOrders().forEach(dishOrder -> {
            dishOrder.setDishOrderStatus(this.dishOrderStatusDAO.findAllByDishOrderId(dishOrder.getId()));
        });
        return savedOrder;
    }

    public Order updateDishOrders(Long reference, Order orderToUpdate){
        if(orderToUpdate.getActualStatus().getStatus() != Status.CREATED
                && orderToUpdate.getActualStatus().getStatus() != Status.CONFIRMED){
            throw new ClientException("The order status must be CREATED OR CONFIRMED!   ");
        }
        List<DishOrder> dishOrders = orderToUpdate.getDishOrders();
        Order order = orderDAO.findByReference(reference);
        order.setOrderStatus(this.orderStatusDAO.findAllByOrderId(order.getId()));
        if(order.getActualStatus().getStatus() != Status.CREATED){
            throw new RuntimeException("Can't update dishOrders because this actual status of the order is not created!");
        }
        order.getDishOrders().clear();
        dishOrders.forEach(dishOrder -> {
            if(this.dishDAO.findByName(dishOrder.getDish().getName()) == null){
                throw new RuntimeException(dishOrder.getDish().getName()+" not exists");
            }
            dishOrder.setDish(this.dishDAO.findByName(dishOrder.getDish().getName()));
            order.addDishOrder(dishOrder);
            DishOrder savedDishOrder = this.dishOrderDAO.save(dishOrder);
            DishOrderStatus dishOrderStatus = new DishOrderStatus();
            dishOrderStatus.setDishOrder(savedDishOrder);
            dishOrderStatus.setStatusDate(LocalDateTime.now());
            dishOrderStatus.setStatus(orderToUpdate.getOrderStatus().getFirst().getStatus());
            this.dishOrderStatusDAO.saveDishOrderStatus(dishOrderStatus);
        });
        Order savedOrder = orderDAO.save(order);
        savedOrder.setDishOrders(this.dishOrderDAO.findAllByOrderId(savedOrder.getId()));
        orderToUpdate.getActualStatus().setOrder(savedOrder);
//        this.orderStatusDAO.findAllByOrderId();
        savedOrder.setOrderStatus(List.of(this.orderStatusDAO.saveOrderStatus(orderToUpdate.getActualStatus())));
        return savedOrder;
    }
}
