package com.williest.td2springbootrestaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DishOrderStatus extends EntityStatus{
    @JsonIgnore
    private DishOrder dishOrder;
}
