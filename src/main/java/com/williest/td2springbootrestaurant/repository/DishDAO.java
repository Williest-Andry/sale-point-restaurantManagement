package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.model.Dish;
import com.williest.td2springbootrestaurant.model.Ingredient;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishDAO implements EntityDAO<Dish> {
    private DataSourceDB dataSourceDB;
    private  PriceDAO priceDAO;
    private StockMovementDAO stockMovementDAO;
    private String sqlRequest;

    public DishDAO(DataSourceDB dataSourceDB) {
        this.dataSourceDB = dataSourceDB;
    }

    @Override
    public Dish findById(long id , LocalDateTime chosenDate, LocalDateTime moveDate) {
        Dish dish = null;
        IngredientDAO ingredientDAO = new IngredientDAO(dataSourceDB);

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
                Ingredient ingredient = ingredientDAO.findById(rs.getLong("ingredient_id"));
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
        IngredientDAO ingredientDAO = new IngredientDAO(dataSourceDB);

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
                Ingredient ingredient = ingredientDAO.findById(rs.getLong("ingredient_id"));
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

    public List<Dish> findBestSales(LocalDateTime startDate, LocalDateTime endDate, int numberOfDishes){
        List<Dish> bestDishes = new ArrayList<>();
        try(Connection dbConnection = dataSourceDB.getConnection()){
            sqlRequest = "select name, count(order_status) as total_sales, (dish_quantity* dish.unit_price) AS total_amount from order_status " +
                    "join dish_order on dish_order.id=order_status.order_id " +
                    "join dish on dish.id=dish_order.dish_id group by name, dish.unit_price, dish_quantity order by total_sales desc limit ?;";
        } catch(SQLException e){
            e.printStackTrace();
        }
        return bestDishes;
    }
}

