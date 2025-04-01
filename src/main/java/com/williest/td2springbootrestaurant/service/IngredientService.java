package com.williest.td2springbootrestaurant.service;

import com.williest.td2springbootrestaurant.entity.Ingredient;
import com.williest.td2springbootrestaurant.repository.IngredientDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {
    IngredientDAO ingredientDAO ;

    public IngredientService (IngredientDAO ingredientDAO){
        this.ingredientDAO = ingredientDAO;
    }

    public List<Ingredient> getAllIngredients(Double priceMinFilter, Double priceMaxFilter) {
        if((priceMinFilter != null && priceMaxFilter != null) && (priceMinFilter < 0 || priceMaxFilter < 0)) {
            throw new RuntimeException("priceMinFilter et/ou priceMaxFilter ne peut pas être négatif!");
        }
        else if((priceMinFilter != null && priceMaxFilter != null) && (priceMinFilter > priceMaxFilter)) {
            throw new RuntimeException("priceMinFilter= "+priceMinFilter+" ne peut pas être supérieur à priceMaxFilter" +
                    ": "+priceMaxFilter);
        }
        return ingredientDAO.findAll(1,10).stream()
                .filter(ingredient -> {
                    if(priceMinFilter == null && priceMaxFilter != null){
                        return ingredient.getUnitPrice() <= priceMaxFilter;
                    }
                    else if (priceMaxFilter == null && priceMinFilter != null){
                        return ingredient.getUnitPrice() >= priceMinFilter;
                    }
                    else if (priceMinFilter == null && priceMaxFilter == null){
                        return true;
                    }
                    else {
                        return ingredient.getUnitPrice() >= priceMinFilter && ingredient.getUnitPrice() <= priceMaxFilter;
                    }
                })
                .toList();
    }
}
