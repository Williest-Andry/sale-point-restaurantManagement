package com.williest.td2springbootrestaurant.restController.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.williest.td2springbootrestaurant.model.DishOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@AllArgsConstructor
public class DishOrderRest {
    private int dishQuantity;
    @JsonIgnore
    private List<DishOrderStatus> dishOrderStatus = new ArrayList<>();

    public DishOrderStatus getActualStatus(){
        return dishOrderStatus.stream().max(Comparator.comparing(DishOrderStatus::getStatusDate))
                .orElse(null);
    }
}
