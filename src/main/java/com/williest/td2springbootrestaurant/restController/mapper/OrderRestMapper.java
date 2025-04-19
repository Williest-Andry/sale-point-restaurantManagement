package com.williest.td2springbootrestaurant.restController.mapper;

import com.williest.td2springbootrestaurant.model.DishOrder;
import com.williest.td2springbootrestaurant.model.Order;
import com.williest.td2springbootrestaurant.model.OrderStatus;
import com.williest.td2springbootrestaurant.restController.rest.CreateOrder;
import com.williest.td2springbootrestaurant.restController.rest.DishOrderRest;
import com.williest.td2springbootrestaurant.restController.rest.OrderBasicRest;
import com.williest.td2springbootrestaurant.restController.rest.OrderRest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class OrderRestMapper implements Function<Order, OrderRest> {
    private final DishOrderRestMapper dishOrderRestMapper;

    @Override
    public OrderRest apply(Order order) {
        List<DishOrderRest> dishOrderRest = order.getDishOrders().stream().map(dishOrderRestMapper::apply).toList();
        OrderRest orderRest = new OrderRest(
                order.getReference(),
                dishOrderRest,
                order.getOrderStatus()
        );
        return orderRest;
    }

    public Order toModel(CreateOrder createOrder){
        List<DishOrder> dishOrders = createOrder.getDishOrders().stream().map(dishOrderRestMapper::toModel).toList();
        Order order = new Order();
        order.setDishOrders(dishOrders);
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setStatus(createOrder.getOrderStatus().getStatus());
        order.setOrderStatus(List.of(orderStatus));
        return order;
    }

    public OrderBasicRest toBasicRest(Order order){
        return new OrderBasicRest(
                order.getId(),
                order.getReference(),
                order.getOrderDate()
        );
    }
}
