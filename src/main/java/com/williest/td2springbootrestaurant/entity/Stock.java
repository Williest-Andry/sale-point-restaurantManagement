package com.williest.td2springbootrestaurant.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Stock {
    private long id;
    private Ingredient ingredient;
    private MoveType moveType;
    private LocalDateTime moveDate;
}
