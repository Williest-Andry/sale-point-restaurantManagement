package com.williest.td2springbootrestaurant.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Price {
    private long id;
    private Ingredient ingredient;
    private Double amount = 0.0;
    private LocalDateTime beginDate;
}