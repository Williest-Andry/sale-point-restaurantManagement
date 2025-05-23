package com.williest.td2springbootrestaurant.service;

import com.williest.td2springbootrestaurant.model.Ingredient;
import com.williest.td2springbootrestaurant.model.Price;
import com.williest.td2springbootrestaurant.model.StockMovement;
import com.williest.td2springbootrestaurant.repository.IngredientDAO;
import com.williest.td2springbootrestaurant.repository.PriceDAO;
import com.williest.td2springbootrestaurant.repository.StockMovementDAO;
import com.williest.td2springbootrestaurant.service.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class IngredientService {
    private final IngredientDAO ingredientDAO;
    private final PriceDAO priceDAO;
    private final StockMovementDAO stockMovementDAO;

    public List<Ingredient> getAllIngredients(Double priceMinFilter, Double priceMaxFilter) {
        if((priceMinFilter != null && priceMaxFilter != null) && (priceMinFilter < 0 || priceMaxFilter < 0)) {
            throw new ClientException("priceMinFilter or priceMaxFilter can't be negative!");
        }
        else if((priceMinFilter != null && priceMaxFilter != null) && (priceMinFilter > priceMaxFilter)) {
            throw new ClientException("priceMinFilter= "+priceMinFilter+" can't be greater than priceMaxFilter" +
                    ": "+priceMaxFilter);
        }

        return ingredientDAO.findAll(1,10).stream()
                .filter(ingredient -> {
                    ingredient.setPrices(priceDAO.findAllByIngredientId(ingredient.getId()));
                    ingredient.setStocksMovement(stockMovementDAO.findAllByIngredientId(ingredient.getId()));
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

    public Ingredient save(Ingredient ingredient){
        Ingredient savedIngredient = ingredientDAO.save(ingredient);
        List<Price> savedPrices = priceDAO.saveAll(ingredient.getPrices());
        List<Price> allPrices = priceDAO.findAllByIngredientId(ingredient.getId());
        List<StockMovement> savedStockMovements= stockMovementDAO.saveAll(ingredient.getStocksMovement());
        List<StockMovement> allStockMovements = stockMovementDAO.findAllByIngredientId(ingredient.getId());
        savedIngredient.setPrices(allPrices);
        savedIngredient.setStocksMovement(allStockMovements);
        return savedIngredient;
    }

    public Ingredient addPrices(Long ingredientId, List<Price> prices){
        Ingredient ingredient = ingredientDAO.findById(ingredientId);
        ingredient.addPrices(prices);
        return this.save(ingredient);
    }

    public Ingredient addStockMovements(Long ingredientId, List<StockMovement> stockMovements){
        Ingredient ingredient = ingredientDAO.findById(ingredientId);
        ingredient.addStockMovement(stockMovements);
        return this.save(ingredient);
    }
}
