package com.williest.td2springbootrestaurant.restController.rest;

import com.williest.td2springbootrestaurant.model.EntityStatus;
import com.williest.td2springbootrestaurant.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderRest {
    private Long reference;
    private List<DishOrderRest> dishOrders;
    private List<OrderStatus> orderStatus = new ArrayList<>();

    public EntityStatus getActualStatus() {
        return orderStatus.stream().max(Comparator.comparing(EntityStatus::getStatusDate))
                .orElse(null);
    }
}
