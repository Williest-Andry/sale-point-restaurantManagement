package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.model.Ingredient;
import com.williest.td2springbootrestaurant.model.StockMovementType;
import com.williest.td2springbootrestaurant.model.StockMovement;
import com.williest.td2springbootrestaurant.model.Unit;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class StockMovementDAO {
    private final DataSourceDB dataSourceDB;
    private String sqlRequest;
    private final IngredientDAO ingredientDAO ;

    public StockMovementDAO(DataSourceDB dataSourceDB, IngredientDAO ingredientDAO) {
        this.dataSourceDB = dataSourceDB;
        this.ingredientDAO = ingredientDAO;
    }

    public List<StockMovement> findAllByIngredientId(long ingredientId){
        List<StockMovement> stockMovements = new ArrayList<>();
        StockMovement stockMovement = null;
        try(Connection dbConnection = dataSourceDB.getConnection()){
            sqlRequest = "SELECT ingredient_move.id AS ingredient_move_id, type, ingredient_quantity, ingredient_move.unit AS ingredient_move_unit, move_date FROM ingredient_move " +
                    "INNER JOIN ingredient ON ingredient.id = ingredient_move.ingredient_id " +
                    "WHERE ingredient_move.ingredient_id = ?";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setLong(1, ingredientId);
            ResultSet rs = select.executeQuery();
            Ingredient ingredient = ingredientDAO.findById(ingredientId);
            while(rs.next()){
                stockMovement = new StockMovement();
                stockMovement.setId(rs.getLong("ingredient_move_id"));
                stockMovement.setIngredient(ingredient);
                stockMovement.setMovementType(StockMovementType.valueOf(rs.getString("type")));
                stockMovement.setMoveDate(rs.getTimestamp("move_date").toLocalDateTime());
                stockMovement.setQuantity(rs.getDouble("ingredient_quantity"));
                stockMovement.setUnit(Unit.valueOf(rs.getString("ingredient_move_unit")));
                stockMovements.add(stockMovement);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return stockMovements;
    }
}
