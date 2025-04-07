package com.williest.td2springbootrestaurant.service;

import com.williest.td2springbootrestaurant.entity.Ingredient;
import com.williest.td2springbootrestaurant.repository.DataSourceDB;
import com.williest.td2springbootrestaurant.repository.IngredientDAO;
import com.williest.td2springbootrestaurant.repository.PriceDAO;
import com.williest.td2springbootrestaurant.repository.StockDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {
    private final IngredientDAO ingredientDAO;

    public IngredientService (IngredientDAO ingredientDAO){
        this.ingredientDAO = ingredientDAO;
    }

    public List<Ingredient> getAllIngredients(Double priceMinFilter, Double priceMaxFilter) {
        System.out.println("IngredientDAO injecté : " + ingredientDAO);
        if((priceMinFilter != null && priceMaxFilter != null) && (priceMinFilter < 0 || priceMaxFilter < 0)) {
            throw new RuntimeException("priceMinFilter or priceMaxFilter can't be negative!");
        }
        else if((priceMinFilter != null && priceMaxFilter != null) && (priceMinFilter > priceMaxFilter)) {
            throw new RuntimeException("priceMinFilter= "+priceMinFilter+" can't be greater than priceMaxFilter" +
                    ": "+priceMaxFilter);
        }
        return ingredientDAO.findAll(1,10).stream()
                .filter(ingredient -> {
                    if(priceMinFilter == null && priceMaxFilter != null){
                        return ingredient.getActualPrice() <= priceMaxFilter;
                    }
                    else if (priceMaxFilter == null && priceMinFilter != null){
                        return ingredient.getActualPrice()  >= priceMinFilter;
                    }
                    else if (priceMinFilter == null && priceMaxFilter == null){
                        return true;
                    }
                    else {
                        return ingredient.getActualPrice()  >= priceMinFilter && ingredient.getActualPrice()  <= priceMaxFilter;
                    }
                })
                .toList();
    }

    public Ingredient getIngredientById(long id){
        return ingredientDAO.findById(id);
    }

    public Ingredient create(Ingredient ingredient){
        return ingredientDAO.save(ingredient);
    }
}
