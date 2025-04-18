package com.williest.td2springbootrestaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Sale {
    private Long id;
    @JsonIgnore
    private Dish dish;
    @JsonIgnore
    private Order order;
    private int quantitySold;
}