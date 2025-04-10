package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.model.Ingredient;
import com.williest.td2springbootrestaurant.model.StockMovementType;
import com.williest.td2springbootrestaurant.model.StockMovement;
import com.williest.td2springbootrestaurant.model.Unit;
import com.williest.td2springbootrestaurant.restController.rest.StockMovementRest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.sql.*;
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

    public StockMovement findById(Long id){
        StockMovement stockMovement = null;
        try(Connection dbConnection = dataSourceDB.getConnection()){
            sqlRequest = "SELECT id, ingredient_id, type, ingredient_quantity, unit, move_date FROM ingredient_move WHERE id=?;";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setLong(1, id);
            ResultSet rs = select.executeQuery();
            if(rs.next()){
                stockMovement = new StockMovement();
                stockMovement.setId(rs.getLong("id"));
                stockMovement.setMovementType(StockMovementType.valueOf(rs.getString("type")));
                stockMovement.setMoveDate(rs.getTimestamp("move_date").toLocalDateTime());
                stockMovement.setQuantity(rs.getDouble("ingredient_quantity"));
                stockMovement.setUnit(Unit.valueOf(rs.getString("unit")));
                stockMovement.setMoveDate(rs.getTimestamp("move_date").toLocalDateTime());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return stockMovement;
    }

    public StockMovement save(StockMovement stockMovement){
        Long stockMovementId = 0L;
        if(stockMovement.getId() != null){
            return stockMovement;
        }
        try(Connection dbConnection = dataSourceDB.getConnection()){
            sqlRequest = "INSERT INTO ingredient_move(ingredient_id, type, ingredient_quantity, unit, move_date) " +
                    "VALUES(?,?::move_type,?,?::unit,?) RETURNING id;";
            PreparedStatement insert = dbConnection.prepareStatement(sqlRequest);
            insert.setLong(1, stockMovement.getIngredient().getId());
            insert.setString(2, stockMovement.getMovementType().toString());
            insert.setDouble(3, stockMovement.getQuantity());
            insert.setString(4, stockMovement.getUnit().toString());
            insert.setTimestamp(5, Timestamp.valueOf(stockMovement.getMoveDate()));
            ResultSet rs = insert.executeQuery();
            if(rs.next()){
                stockMovementId = rs.getLong("id");
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return this.findById(stockMovementId);
    }

    public List<StockMovement> saveAll(List<StockMovement> stockMovements){
        List<StockMovement> savedStocks = new ArrayList<>();
        for(StockMovement stockMovement : stockMovements) {
            StockMovement savedStock = this.save(stockMovement);
            savedStocks.add(savedStock);
        }
        return savedStocks;
    }

    public StockMovement update(StockMovement stockMovement){
        throw new UnsupportedOperationException("not implemeted");
    }
}
