package com.williest.td2springbootrestaurant.restController.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderBasicRest {
    private Long id;
    private String reference;
    private LocalDateTime orderDate;
}
