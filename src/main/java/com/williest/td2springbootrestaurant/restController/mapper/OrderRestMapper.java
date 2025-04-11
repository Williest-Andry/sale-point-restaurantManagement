package com.williest.td2springbootrestaurant.restController.mapper;

import com.williest.td2springbootrestaurant.model.Order;
import com.williest.td2springbootrestaurant.restController.rest.DishOrderRest;
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
}
