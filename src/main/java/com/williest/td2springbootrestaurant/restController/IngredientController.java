package com.williest.td2springbootrestaurant.restController;

import com.williest.td2springbootrestaurant.service.IngredientService;
import com.williest.td2springbootrestaurant.service.exception.ClientException;
import com.williest.td2springbootrestaurant.service.exception.NotFoundException;
import com.williest.td2springbootrestaurant.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class IngredientController {
    private final IngredientService ingredientService;

    @GetMapping("/ingredients")
    public ResponseEntity<Object> getIngredients(@RequestParam(required = false) Double priceMinFilter,
                                                 @RequestParam(required = false) Double priceMaxFilter) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ingredientService.getAllIngredients(priceMinFilter, priceMaxFilter));
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
//    @PutMapping("/ingredient/{ingredientId}/prices")
//    public ResponseEntity<Object> updateIngredientPriceById(@RequestBody List<Price> prices){
//        return ResponseEntity.status(HttpStatus.CREATED).body(priceService.updateByIngredientId(prices));
//    }
//
//    @PutMapping("/ingredient/{ingredientId}/stocks")
//    public ResponseEntity<Object> updateIngredientStockById(@RequestBody List<Stock> stocks){
//        return ResponseEntity.status(HttpStatus.CREATED).body(priceService.updateByIngredientId(stocks));
//    }
}
