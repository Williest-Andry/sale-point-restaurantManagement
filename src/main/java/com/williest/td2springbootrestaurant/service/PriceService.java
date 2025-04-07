package com.williest.td2springbootrestaurant.service;

import com.williest.td2springbootrestaurant.entity.Price;
import com.williest.td2springbootrestaurant.repository.PriceDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PriceService {
    private PriceDAO priceDAO;

    public List<Price> updateByIngredientId(List<Price> prices) {
        return priceDAO.updateByIngredientId(prices);
    }
}
