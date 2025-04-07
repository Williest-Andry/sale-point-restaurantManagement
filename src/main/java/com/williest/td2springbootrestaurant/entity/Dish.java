package com.williest.td2springbootrestaurant.entity;

import java.util.*;

public class Dish {
    private long id;
    private String name;
    private Double unit_price;
    private List<Ingredient> ingredients = new ArrayList<>();
    private HashMap<Ingredient, Double> ingredientQuantities = new HashMap<>();

    public Dish(String name, Double unit_price) {
        this.name = name;
        this.unit_price = unit_price;
    }

//    public int getAvalaibleQuantity() {
//        List<Double> allQuantities = new ArrayList<>();
//        Collections.sort(ingredients, (ingredient1, ingredient2) -> Long.compare(ingredient1.getId(), ingredient2.getId()));
//        List<Double> necessaryQuantities = ingredientQuantities.entrySet()
//                .stream()
//                .sorted((i1, i2) -> Long.compare(i1.getKey().getId(), i2.getKey().getId()))
//                .map(ingredient -> ingredient.getValue())
//                .toList();
//
//        for (int i = 0; i < ingredients.size(); i++) {
//            allQuantities.add(ingredients.get(i).getAvalaibleQuantity() / necessaryQuantities.get(i));
//        }
//
//        return allQuantities.stream().min(Double::compare).orElse(0.0).intValue();
//    }

    public void addToIngredients(Ingredient ingredient){
        ingredients.add(ingredient);
    }

    public void addQuantity(Ingredient ingredient, Double quantity){
        ingredientQuantities.put(ingredient, quantity);
    }

    public double getIngredientCost() {
        double totatCost = 0;
        for (Ingredient ingredient : ingredients) {
            totatCost += ingredientQuantities.get(ingredient) * ingredient.getActualPrice();
        }

        return totatCost;
    }

    public double getGrossMargin() {
        return unit_price - this.getIngredientCost();
    }

    public String getName() {
        return name;
    }

    public Double getUnit_price() {
        return unit_price;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public HashMap<Ingredient, Double> getIngredientQuantities() {
        return ingredientQuantities;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return id == dish.id && Objects.equals(name, dish.name) && Objects.equals(unit_price, dish.unit_price) && Objects.equals(ingredients, dish.ingredients) && Objects.equals(ingredientQuantities, dish.ingredientQuantities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, unit_price, ingredients, ingredientQuantities);
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unit_price=" + unit_price +
                ", ingredients=" + ingredients +
                ", ingredientQuantities=" + ingredientQuantities +
                '}';
    }
}

