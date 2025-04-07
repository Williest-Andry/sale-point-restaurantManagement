package com.williest.td2springbootrestaurant.restController;

import com.williest.td2springbootrestaurant.entity.Price;
import com.williest.td2springbootrestaurant.entity.Stock;
import com.williest.td2springbootrestaurant.repository.IngredientDAO;
import com.williest.td2springbootrestaurant.entity.Ingredient;
import com.williest.td2springbootrestaurant.service.IngredientService;
import com.williest.td2springbootrestaurant.service.PriceService;
import com.williest.td2springbootrestaurant.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class IngredientController {
    private IngredientService ingredientService;
    private IngredientDAO ingredientDAO;
    private PriceService priceService;
    private StockService stockService;

    public IngredientController(IngredientService ingredientService, IngredientDAO ingredientDAO) {
        this.ingredientService = ingredientService;
        this.ingredientDAO = ingredientDAO;
    }

    @GetMapping("/ingredients")
    public ResponseEntity<Object> getIngredients(@RequestParam(required = false) Double priceMinFilter,
                                                 @RequestParam(required = false) Double priceMaxFilter) {
        return ResponseEntity.status(HttpStatus.OK).body(ingredientService.getAllIngredients(priceMinFilter, priceMaxFilter));
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Object> getIngredient(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(ingredientService.getIngredientById(id));
    }

    @PostMapping("/ingredients")
    public ResponseEntity<Object> addIngredient(@RequestBody Ingredient ingredient) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredientService.create(ingredient));
    }

    @PutMapping("/ingredients")
    public ResponseEntity<Object> updateIngredient(@RequestBody Ingredient ingredient) {
        return ResponseEntity.status(HttpStatus.OK).body(ingredientService.create(ingredient));
    }

    @PutMapping("/ingredient/{ingredientId}/prices")
    public ResponseEntity<Object> updateIngredientPriceById(@RequestBody List<Price> prices){
        return ResponseEntity.status(HttpStatus.CREATED).body(priceService.updateByIngredientId(prices));
    }

    @PutMapping("/ingredient/{ingredientId}/stocks")
    public ResponseEntity<Object> updateIngredientStockById(@RequestBody List<Stock> stocks){
        return ResponseEntity.status(HttpStatus.CREATED).body(priceService.updateByIngredientId(stocks));
    }
}
