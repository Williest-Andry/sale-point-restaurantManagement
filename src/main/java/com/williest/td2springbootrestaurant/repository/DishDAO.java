package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.model.Dish;
import com.williest.td2springbootrestaurant.model.Ingredient;
import com.williest.td2springbootrestaurant.restController.rest.DishBestSale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishDAO implements EntityDAO<Dish> {
    private final DataSourceDB dataSourceDB;
    private String sqlRequest;

    @Override
    public Dish findById(long id) {
        Dish dish = null;

        try (Connection dbConnection = dataSourceDB.getConnection()){
            String sqlRequest = "SELECT id, name, unit_price FROM dish WHERE id=?;";
            PreparedStatement selectDish = dbConnection.prepareStatement(sqlRequest);
            selectDish.setLong(1, id);
            ResultSet rs = selectDish.executeQuery();
            if (rs.next()) {
                dish = new Dish();
                dish.setId(rs.getLong("id"));
                dish.setName(rs.getString("name"));
                dish.setUnitPrice(rs.getDouble("unit_price"));
//                dish.setIngredients(dishIngredientDAO.findDishIngredientByDishId(id));
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

        try (Connection dbConnection = dataSourceDB.getConnection()){
            String sqlRequest = "SELECT id, name, unit_price FROM dish;";
            PreparedStatement selectDish = dbConnection.prepareStatement(sqlRequest);
            ResultSet rs = selectDish.executeQuery();
            while (rs.next()) {
                dish = new Dish();
                dish.setId(rs.getLong("id"));
                dish.setName(rs.getString("name"));
                dish.setUnitPrice(rs.getDouble("unit_price"));
//                dish.setIngredients(dishIngredientDAO.findDishIngredientByDishId(rs.getLong("id")));
                dishes.add(dish);
            }
        }
        catch(Exception e){
            throw new RuntimeException("ERROR IN FIND ALL DISHES: " + e);
        }
        return dishes;
    }

    public Dish findByName(String dishName) {
        Dish dish = null;

        try (Connection dbConnection = dataSourceDB.getConnection()){
            String sqlRequest = "SELECT id, name, unit_price FROM dish WHERE name=?;";
            PreparedStatement selectDish = dbConnection.prepareStatement(sqlRequest);
            selectDish.setString(1, dishName);
            ResultSet rs = selectDish.executeQuery();
            if (rs.next()) {
                dish = new Dish();
                dish.setId(rs.getLong("id"));
                dish.setName(rs.getString("name"));
                dish.setUnitPrice(rs.getDouble("unit_price"));
//                dish.setIngredients(dishIngredientDAO.findDishIngredientByDishId(id));
            }
        }
        catch(Exception e){
            throw new RuntimeException("ERROR IN FIND DISH BY NAME: " + e);
        }
        return dish;
    }

    @Override
    public Dish save(Dish dish) {
        if(this.findById(dish.getId()) != null){
            this.update(dish);
        }
        else{
            try(Connection dbconnection = dataSourceDB.getConnection()){
                sqlRequest = "INSERT INTO dish(name, unit_price) VALUES(?,?);";
                PreparedStatement create = dbconnection.prepareStatement(sqlRequest);
                create.setString(1, dish.getName());
                create.setDouble(2, dish.getUnitPrice());
                create.executeUpdate();
//                this.dishIngredientDAO.saveAll(dish.getIngredients());
            }
            catch(SQLException e){
                throw new RuntimeException("CAN'T SAVE THE DISH: ", e);
            }
        }
        return this.findById(dish.getId());
    }

    @Override
    public List<Dish> saveAll(List<Dish> dishes) {
        List<Dish> savedDishes = new ArrayList<>();
        dishes.forEach(this::save);
        for (Dish dish : dishes) {
            savedDishes.add(this.findById(dish.getId()));
        }
        return savedDishes;
    }

    @Override
    public Dish update(Dish dish) {
        try(Connection dbconnection = dataSourceDB.getConnection()){
            sqlRequest = "UPDATE dish SET name=?, unit_price=? WHERE id=?;";
            PreparedStatement update = dbconnection.prepareStatement(sqlRequest);
            update.setString(1, dish.getName());
            update.setDouble(2, dish.getUnitPrice());
            update.setLong(3, dish.getId());
            update.executeUpdate();
//            this.dishIngredientDAO.saveAll(dish.getIngredients());
        }
        catch(SQLException e){
            throw new RuntimeException("CAN'T UPDATE THE DISH: ", e);
        }
        return this.findById(dish.getId());
    }

    @Override
    public Dish deleteById(String id) {
        throw new UnsupportedOperationException("not implemented");
    }

    public List<Dish> findBestSales(LocalDateTime startDate, LocalDateTime endDate, int numberOfDishes){
        List<Dish> bestDishes = new ArrayList<>();
        try(Connection dbConnection = dataSourceDB.getConnection()){
            sqlRequest = "SELECT name, COUNT(order_status) AS total_sales, (dish_quantity* dish.unit_price) AS total_amount FROM order_status " +
                    "JOIN dish_order ON dish_order.id=order_status.order_id " +
                    "JOIN dish ON dish.id=dish_order.dish_id " +
                    "WHERE order_status = 'DONE' AND order_status_date > ? AND order_status_date < ? " +
                    "GROUP BY name, dish.unit_price, dish_quantity " +
                    "ORDER BY total_sales DESC LIMIT ?;";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setTimestamp(1, Timestamp.valueOf(startDate));
            select.setTimestamp(2, Timestamp.valueOf(endDate));
            select.setInt(3, numberOfDishes);
            ResultSet rs = select.executeQuery();
            while (rs.next()) {
                DishBestSale dish = new DishBestSale();
                dish.setName(rs.getString("name"));
                dish.setSalesQuantity(rs.getInt("total_sales"));
                dish.setTotalAmount(rs.getDouble("total_amount"));
                dish.setIngredients(new ArrayList<>());
                dish.setUnitPrice(0.0);

                bestDishes.add(dish);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return bestDishes;
    }
}

