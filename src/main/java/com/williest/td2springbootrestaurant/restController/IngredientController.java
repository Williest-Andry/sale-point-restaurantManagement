package com.williest.td2springbootrestaurant.restController;

import com.williest.td2springbootrestaurant.model.Ingredient;
import com.williest.td2springbootrestaurant.model.Price;
import com.williest.td2springbootrestaurant.model.StockMovement;
import com.williest.td2springbootrestaurant.restController.mapper.IngredientRestMapper;
import com.williest.td2springbootrestaurant.restController.mapper.PriceRestMapper;
import com.williest.td2springbootrestaurant.restController.mapper.StockMovementRestMapper;
import com.williest.td2springbootrestaurant.restController.rest.CreateIngredientPrice;
import com.williest.td2springbootrestaurant.restController.rest.CreateStockMovement;
import com.williest.td2springbootrestaurant.restController.rest.IngredientRest;
import com.williest.td2springbootrestaurant.service.IngredientService;
import com.williest.td2springbootrestaurant.service.PriceService;
import com.williest.td2springbootrestaurant.service.exception.ClientException;
import com.williest.td2springbootrestaurant.service.exception.NotFoundException;
import com.williest.td2springbootrestaurant.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class IngredientController {
    private final IngredientService ingredientService;
    private final PriceService priceService;
    private final PriceRestMapper priceRestMapper;
    private final IngredientRestMapper ingredientRestMapper;
    private final StockMovementRestMapper stockMovementRestMapper;

    @GetMapping("/ingredients")
    public ResponseEntity<Object> getIngredients(@RequestParam(required = false) Double priceMinFilter,
                                                 @RequestParam(required = false) Double priceMaxFilter) {
        try {
            List<Ingredient> ingredients = ingredientService.getAllIngredients(priceMinFilter, priceMaxFilter);
            List<IngredientRest> ingredientsRest = ingredients.stream().map(ingredientRestMapper::apply).toList();
            return ResponseEntity.status(HttpStatus.OK).body(ingredientsRest);
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

//    @GetMapping("/ingredients/{id}")
//    public ResponseEntity<Object> getIngredient(@PathVariable long id) {
//        return ResponseEntity.status(HttpStatus.OK).body(ingredientService.getIngredientById(id));
//    }
//
//    @PostMapping("/ingredients")
//    public ResponseEntity<Object> addIngredient(@RequestBody Ingredient ingredient) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(ingredientService.create(ingredient));
//    }
//
//    @PutMapping("/ingredients")
//    public ResponseEntity<Object> updateIngredient(@RequestBody Ingredient ingredient) {
//        return ResponseEntity.status(HttpStatus.OK).body(ingredientService.create(ingredient));
//    }
//
    @PutMapping("/ingredients/{ingredientId}/prices")
    public ResponseEntity<Object> updateIngredientPrices(@PathVariable Long ingredientId,
                                                            @RequestBody List<CreateIngredientPrice> pricesToCreate){
        try{
            List<Price> prices = pricesToCreate.stream().map(priceRestMapper::toModel).toList();
            Ingredient ingredient = ingredientService.addPrices(ingredientId, prices);
            IngredientRest ingredientRest = ingredientRestMapper.apply(ingredient);
            return ResponseEntity.status(HttpStatus.CREATED).body(ingredientRest);
        } catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PutMapping("/ingredients/{ingredientId}/stockMovements")
    public ResponseEntity<Object> updateIngredientStocks(@PathVariable Long ingredientId,
                                                            @RequestBody List<CreateStockMovement> stocksToCreate){
        try{
            List<StockMovement> stockMovements = stocksToCreate.stream().map(stockMovementRestMapper::toModel).toList();
            Ingredient ingredient = ingredientService.addStockMovements(ingredientId, stockMovements);
            IngredientRest ingredientRest  = ingredientRestMapper.apply(ingredient);
            return ResponseEntity.status(HttpStatus.CREATED).body(ingredientRest);
        } catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }
}
