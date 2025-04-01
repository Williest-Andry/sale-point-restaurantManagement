package com.williest.td2springbootrestaurant.restController;

import com.williest.td2springbootrestaurant.repository.CustomDataSource;
import com.williest.td2springbootrestaurant.repository.IngredientDAO;
import com.williest.td2springbootrestaurant.entity.Ingredient;
import com.williest.td2springbootrestaurant.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class IngredientController {
    IngredientService service;
    IngredientDAO ingredientDAO;

    public IngredientController(IngredientService service, IngredientDAO ingredientDAO) {
        this.service = service;
        this.ingredientDAO = ingredientDAO;
    }

    @GetMapping("/ingredients")
    public ResponseEntity<Object> getIngredients(@RequestParam(required = false) Double priceMinFilter,
                                                           @RequestParam(required = false) Double priceMaxFilter) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllIngredients(priceMinFilter, priceMaxFilter));
    }

    @GetMapping("/ingredients/{id}")
    public Ingredient getIngredient(@PathVariable int id) {
        return ingredientDAO.findById(id, null, null);
    }

    @PostMapping("/ingredients")
    public ResponseEntity<Object> addIngredient(@RequestBody Ingredient ingredient) {
        if(ingredient == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'ingredient ne peut pas être null!");
        }
        Ingredient createdIngredient = ingredientDAO.save(ingredient);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIngredient);
    }

    @PutMapping("/ingredients")
    public ResponseEntity<Object> updateIngredient(@RequestBody Ingredient ingredient) {
        if(ingredient == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'ingredient ne peut pas être null!");
        }
        Ingredient updatedIngredient = ingredientDAO.save(ingredient);
        return ResponseEntity.status(HttpStatus.OK).body(updatedIngredient);
    }
}
