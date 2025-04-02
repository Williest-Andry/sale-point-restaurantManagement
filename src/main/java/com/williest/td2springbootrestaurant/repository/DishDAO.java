package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.entity.Dish;
import com.williest.td2springbootrestaurant.entity.Ingredient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DishDAO implements EntityDAO<Dish> {
    private DataSourceDB dataSourceDB;
    private  PriceDAO priceDAO;
    private  StockDAO stockDAO;

    public DishDAO(DataSourceDB dataSourceDB) {
        this.dataSourceDB = dataSourceDB;
    }

    @Override
    public Dish findById(long id , LocalDateTime chosenDate, LocalDateTime moveDate) {
        Dish dish = null;
        IngredientDAO ingredientDAO = new IngredientDAO(dataSourceDB, priceDAO, stockDAO);

        try (Connection dbConnection = dataSourceDB.getConnection()){
            String sqlRequest = "SELECT dish_id, dish.name as dish_name, dish.unit_price as dish_unit_price, ingredient_id, " +
                    "ingredient.name, ingredient_quantity FROM dish_ingredient " +
                    "INNER JOIN dish ON dish.id = dish_id INNER JOIN ingredient ON ingredient.id = ingredient_id " +
                    "WHERE dish.id=?;";
            PreparedStatement selectDish = dbConnection.prepareStatement(sqlRequest);
            selectDish.setLong(1, id);
            ResultSet rs = selectDish.executeQuery();
            while(rs.next()) {
                if(dish == null){
                    dish = new Dish(
                            rs.getString("dish_name"),
                            rs.getDouble("dish_unit_price")
                    );
                    dish.setId(rs.getInt("dish_id"));
                }
                Ingredient ingredient = ingredientDAO.findById(rs.getInt("ingredient_id"));
                dish.addToIngredients(ingredient);
                dish.addQuantity(ingredient, rs.getDouble("ingredient_quantity"));
            }
        }
        catch(Exception e){
            throw new RuntimeException("ERROR IN FIND DISH BY ID: " + e);
        }
        return dish;
    }

    @Override
    public List<Dish> findAll(int page, int size) {
        List<Dish> dishes = new ArrayList<>();
        Dish dish = null;
        IngredientDAO ingredientDAO = new IngredientDAO(dataSourceDB, priceDAO, stockDAO);

        try (Connection dbConnection = dataSourceDB.getConnection()){
            String sqlRequest = "SELECT dish_id, dish.name as dish_name, dish.unit_price as dish_unit_price, ingredient_id, " +
                    "ingredient.name, ingredient_quantity FROM dish_ingredient " +
                    "INNER JOIN dish ON dish.id = dish_id INNER JOIN ingredient ON ingredient.id = ingredient_id ";
            PreparedStatement selectDish = dbConnection.prepareStatement(sqlRequest);
            ResultSet rs = selectDish.executeQuery();
            while(rs.next()) {
                if(dish == null){
                    dish = new Dish(
                            rs.getString("dish_name"),
                            rs.getDouble("dish_unit_price")
                    );
                    dish.setId(rs.getInt("dish_id"));
                }
                Ingredient ingredient = ingredientDAO.findById(rs.getInt("ingredient_id"));
                dish.addToIngredients(ingredient);
                dish.addQuantity(ingredient, rs.getDouble("ingredient_quantity"));
                if(!dishes.contains(dish)){
                    dishes.add(dish);
                }
            }
        }
        catch(Exception e){
            throw new RuntimeException("ERROR IN FIND DISH BY ID: " + e);
        }
        return dishes;
    }

    @Override
    public Dish save(Dish entity) {
        return null;
    }

    @Override
    public Dish update(Dish entity) {
        return null;
    }

    @Override
    public Dish deleteById(String id) {
        return null;
    }
}

