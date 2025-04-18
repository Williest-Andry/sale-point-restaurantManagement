package com.williest.td2springbootrestaurant.restController.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.williest.td2springbootrestaurant.model.DishOrderStatus;
import com.williest.td2springbootrestaurant.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@AllArgsConstructor
public class DishOrderRest {
    private String name;
    private Double unitPrice;
    private int dishQuantity;
    @JsonIgnore
    private List<DishOrderStatus> dishOrderStatus = new ArrayList<>();

    public DishOrderStatus getActualStatus() {
        DishOrderStatus defaultStatus = new DishOrderStatus();
        defaultStatus.setStatus(Status.CREATED);
        return dishOrderStatus.stream().max(Comparator.comparing(DishOrderStatus::getStatusDate))
                .orElse(defaultStatus);
    }
}
