package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.model.Dish;
import com.williest.td2springbootrestaurant.model.DishIngredient;
import com.williest.td2springbootrestaurant.model.Ingredient;
import com.williest.td2springbootrestaurant.model.Unit;
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
            sqlRequest = "SELECT dish_ingredient.id AS dish_ingredient_id, dish_id, ingredient_id, ingredient_quantity," +
                    "  ingredient.unit ingredient_unit, name, latest_modification " +
                    "FROM dish_ingredient " +
                    "JOIN ingredient ON ingredient.id = dish_ingredient.ingredient_id " +
                    "WHERE dish_ingredient.id=?;";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setLong(1, id);
            ResultSet rs = select.executeQuery();
            if(rs.next()){
                dishIngredient = new DishIngredient();
                dishIngredient.setId(rs.getLong("dish_ingredient_id"));
                dishIngredient.setRequiredQuantity(rs.getDouble("ingredient_quantity"));

                Ingredient ingredient = new Ingredient();
                ingredient.setId(rs.getLong("ingredient_id"));
                ingredient.setName(rs.getString("name"));
                ingredient.setUnit(Unit.valueOf(rs.getString("ingredient_unit")));
                ingredient.setLatestModification(rs.getTimestamp("latest_modification").toLocalDateTime());

                dishIngredient.setIngredient(ingredient);
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
            sqlRequest = "SELECT dish_ingredient.id, ingredient.name AS ingredient_name, ingredient_id " +
                    "FROM dish_ingredient " +
                    "JOIN ingredient ON ingredient.id = dish_ingredient.ingredient_id " +
                    "WHERE dish_id = ?;";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setLong(1, id);
            ResultSet rs = select.executeQuery();
            while (rs.next()) {
                dishIngredient = this.findById(rs.getLong("id"));
                dishIngredient.setName(rs.getString("ingredient_name"));

                Dish dish = new Dish();
                dish.setId(id);
                dishIngredient.setDish(dish);

                Ingredient ingredient = new Ingredient();
                ingredient.setId(rs.getLong("ingredient_id"));
                dishIngredient.setIngredient(ingredient);

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
        Long dishIngredientId = 0L;
        if(dishIngredient.getId() != null){
            this.update(dishIngredient);
        }
        else{
            try(Connection dbconnection = dataSourceDB.getConnection()){
                sqlRequest = "INSERT INTO dish_ingredient(dish_id, ingredient_id, ingredient_quantity, unit) " +
                        "VALUES(?,?,?,?::unit) RETURNING id;";
                PreparedStatement create = dbconnection.prepareStatement(sqlRequest);
                create.setLong(1, dishIngredient.getDish().getId());
                create.setLong(2, dishIngredient.getIngredient().getId());
                create.setDouble(3, dishIngredient.getRequiredQuantity());
                create.setString(4, dishIngredient.getIngredient().getUnit().toString());
                ResultSet rs = create.executeQuery();
                if(rs.next()){
                    dishIngredientId = rs.getLong("id");
                }
            }
            catch(SQLException e){
                throw new RuntimeException("CAN'T SAVE THE INGREDIENT OF THE DISH: ", e);
            }
        }
        return this.findById(dishIngredientId);
    }

    @Override
    public List<DishIngredient> saveAll(List<DishIngredient> dishIngredients) {
        return dishIngredients.stream().map(this::save).toList();
    }

    @Override
    public DishIngredient update(DishIngredient dishIngredient) {
        try(Connection dbconnection = dataSourceDB.getConnection()){
            sqlRequest = "UPDATE dish_ingredient SET dish_id=?, ingredient_id=?, ingredient_quantity=? WHERE id=?;";
            PreparedStatement update = dbconnection.prepareStatement(sqlRequest);
            update.setLong(1, dishIngredient.getDish().getId());
            update.setLong(2, dishIngredient.getIngredient().getId());
            update.setDouble(3, dishIngredient.getRequiredQuantity());
            update.setLong(4, dishIngredient.getId());
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
