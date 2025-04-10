package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.model.DishIngredient;
import com.williest.td2springbootrestaurant.model.Ingredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class DishIngredientDAO implements EntityDAO<DishIngredient> {
    private final DataSourceDB dataSourceDB;
    private String sqlRequest;


    @Override
    public DishIngredient findById(long id) {
        DishIngredient dishIngredient= null;

        try(Connection dbConnection = dataSourceDB.getConnection()){
            sqlRequest = "SELECT id, dish_id, ingredient_id, ingredient_quantity, unit FROM dish_ingredient WHERE id=?;";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setLong(1, id);
            ResultSet rs = select.executeQuery();
            if(rs.next()){
                dishIngredient = new DishIngredient();
                dishIngredient.setId(rs.getLong("id"));
                dishIngredient.setRequiredQuantity(rs.getDouble("ingredient_quantity"));
//                Ingredient ingredient = this.ingredientDAO.findById(rs.getLong("ingredient_id"));
//                dishIngredient.setName(ingredient.getName());
//                dishIngredient.setIngredient(ingredient);
//                dishIngredient.setDish(this.dishDAO.findById(rs.getLong("dish_id")));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return dishIngredient;
    }

    public List<DishIngredient> findDishIngredientByDishId(Long id) {
        List<DishIngredient> dishIngredients = new ArrayList<>();
        DishIngredient dishIngredient = null;

        try (Connection dbConnection = dataSourceDB.getConnection()) {
            sqlRequest = "SELECT dish_ingredient.id, ingredient.name AS ingredient_name FROM dish_ingredient " +
                    "JOIN ingredient ON ingredient.id = dish_ingredient.ingredient_id " +
                    "WHERE dish_id = ?;";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setLong(1, id);
            ResultSet rs = select.executeQuery();
            while (rs.next()) {
                dishIngredient = this.findById(rs.getLong("id"));
                dishIngredient.setName(rs.getString("ingredient_name"));
                dishIngredients.add(dishIngredient);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("CAN'T FIND ALL INGREDIENTS OF THE DISH: ",e);
        }
        return dishIngredients;
    }

    @Override
    public List<DishIngredient> findAll(int page, int size) {
        DishIngredient dishIngredient= null;
        List<DishIngredient> dishIngredients = new ArrayList<>();

        try(Connection dbConnection = dataSourceDB.getConnection()){
            sqlRequest = "SELECT id, dish_id, ingredient_id, ingredient_quantity, unit FROM dish_ingredient";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            ResultSet rs = select.executeQuery();
            while(rs.next()){
                dishIngredient = new DishIngredient();
                dishIngredient.setId(rs.getLong("id"));
                dishIngredient.setRequiredQuantity(rs.getDouble("ingredient_quantity"));
//                Ingredient ingredient = this.ingredientDAO.findById(rs.getLong("ingredient_id"));
//                dishIngredient.setName(ingredient.getName());
//                dishIngredient.setIngredient(ingredient);
//                dishIngredients.add(dishIngredient);
//                dishIngredient.setDish(this.dishDAO.findById(rs.getLong("dish_id")));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return dishIngredients;
    }

    @Override
    public DishIngredient save(DishIngredient dishIngredient) {
        if(this.findById(dishIngredient.getId()) != null){
            this.update(dishIngredient);
        }
        else{
            try(Connection dbconnection = dataSourceDB.getConnection()){
                sqlRequest = "INSERT INTO dish_ingredient(dish_id, ingredient_id, ingredient_quantity, unit) VALUES(?,?,?,?);";
                PreparedStatement create = dbconnection.prepareStatement(sqlRequest);
                create.setLong(1, dishIngredient.getDish().getId());
                create.setLong(2, dishIngredient.getIngredient().getId());
                create.setDouble(3, dishIngredient.getRequiredQuantity());
                create.setString(4, dishIngredient.getIngredient().getUnit().toString());
                create.executeUpdate();
            }
            catch(SQLException e){
                throw new RuntimeException("CAN'T SAVE THE INGREDIENT OF THE DISH: ", e);
            }
        }
        return this.findById(dishIngredient.getId());
    }

    @Override
    public List<DishIngredient> saveAll(List<DishIngredient> dishIngredients) {
        List<DishIngredient> savedDishIngredients = new ArrayList<>();
        dishIngredients.forEach(this::save);
        for (DishIngredient dishIngredient : dishIngredients) {
            savedDishIngredients.add(this.findById(dishIngredient.getId()));
        }
        return savedDishIngredients;
    }

    @Override
    public DishIngredient update(DishIngredient dishIngredient) {
        try(Connection dbconnection = dataSourceDB.getConnection()){
            sqlRequest = "UPDATE dish_ingredient SET dish_id=?, ingredient_id=?, ingredient_quantity=? WHERE id=?;";
            PreparedStatement update = dbconnection.prepareStatement(sqlRequest);
            update.setLong(1, dishIngredient.getDish().getId());
            update.setLong(2, dishIngredient.getIngredient().getId());
            update.setDouble(3, dishIngredient.getRequiredQuantity());
            update.executeUpdate();
        }
        catch(SQLException e){
            throw new RuntimeException("CAN'T UPDATE THE INGREDIENT OF THE DISH: ", e);
        }
        return this.findById(dishIngredient.getId());
    }

    @Override
    public DishIngredient deleteById(String id) {
        return null;
    }
}
