package com.williest.td2springbootrestaurant.restController.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PriceRest {
    private Long id;
    private Double amount;
    private LocalDateTime beginDate;
}
