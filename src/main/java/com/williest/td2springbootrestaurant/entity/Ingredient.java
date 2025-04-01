package com.williest.td2springbootrestaurant.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Ingredient {
    private long id;
    private String name;
    private LocalDateTime latestModification;
    private Double unitPrice;
    private Unit unit;
    private Double storageTotalIngredient = 0.0;
    private Double usedTotalIngredient = 0.0;
    private List<Price> prices = new ArrayList<>();
    private List<Stock> stocks = new ArrayList<Stock>();

    public Ingredient(String name, LocalDateTime latestModification, Double unitPrice, Unit unit) {
        this.name = name;
        this.latestModification = latestModification;
        this.unitPrice = unitPrice;
        this.unit = unit;
    }

    public Double getAvalaibleQuantity() {
        return storageTotalIngredient - usedTotalIngredient;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getLatestModification() {
        return latestModification;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getStorageTotalIngredient() {
        return storageTotalIngredient;
    }

    public void setStorageTotalIngredient(Double storageTotalIngredient) {
        this.storageTotalIngredient = storageTotalIngredient;
    }

    public Double getUsedTotalIngredient() {
        return usedTotalIngredient;
    }

    public void setUsedTotalIngredient(Double usedTotalIngredient) {
        this.usedTotalIngredient = usedTotalIngredient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(latestModification, that.latestModification) && Objects.equals(unitPrice, that.unitPrice) && unit == that.unit && Objects.equals(storageTotalIngredient, that.storageTotalIngredient) && Objects.equals(usedTotalIngredient, that.usedTotalIngredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, latestModification, unitPrice, unit, storageTotalIngredient, usedTotalIngredient);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", latestModification=" + latestModification +
                ", unitPrice=" + unitPrice +
                ", unit=" + unit +
                ", storageTotalIngredient=" + storageTotalIngredient +
                ", usedTotalIngredient=" + usedTotalIngredient +
                '}';
    }
}

