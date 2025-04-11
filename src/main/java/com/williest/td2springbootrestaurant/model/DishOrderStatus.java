package com.williest.td2springbootrestaurant.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DishOrderStatus extends EntityStatus{
    private DishOrder dishOrder;
}
