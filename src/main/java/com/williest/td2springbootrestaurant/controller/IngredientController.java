package com.williest.td2springbootrestaurant.controller;

import com.williest.td2springbootrestaurant.dao.DataSource;
import com.williest.td2springbootrestaurant.dao.IngredientDAO;
import com.williest.td2springbootrestaurant.entity.Ingredient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IngredientController {
    IngredientDAO ingredientDAO = new IngredientDAO(new DataSource());

    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getIngredients() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ingredientDAO.findAll(1,5));
    }

    @GetMapping("/ingredients/{id}")
    public Ingredient getIngredient(@PathVariable int id) {
        return ingredientDAO.findById(id, null, null);
    }
}
