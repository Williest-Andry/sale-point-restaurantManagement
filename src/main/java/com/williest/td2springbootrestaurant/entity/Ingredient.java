package com.williest.td2springbootrestaurant.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Ingredient {
    private long id;
    private String name;
    private LocalDateTime latestModification;
    private Unit unit;
    private List<Price> prices = new ArrayList<>();
    private List<Stock> stocks = new ArrayList<>();

    public Ingredient(String name, LocalDateTime latestModification, Unit unit) {
        this.name = name;
        this.latestModification = latestModification;
        this.unit = unit;
    }

    public Double getAvalaibleQuantity() {
       throw new UnsupportedOperationException("not implemented");
    }

    public Double getActualPrice() {
        throw new UnsupportedOperationException("not implemented");
    }
}

