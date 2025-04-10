package com.williest.td2springbootrestaurant.model;

import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Price {
    private Long id;
    private Ingredient ingredient;
    private Double amount;
    private LocalDateTime beginDate;
}