package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.entity.Ingredient;
import com.williest.td2springbootrestaurant.entity.MoveType;
import com.williest.td2springbootrestaurant.entity.Stock;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StockDAO {
    private final DataSourceDB dataSourceDB;
    private String sqlRequest;
    private IngredientDAO ingredientDAO ;

    public StockDAO(DataSourceDB dataSourceDB) {
        this.dataSourceDB = dataSourceDB;
    }

    public List<Stock> findAllByIngredientId(long ingredientId){
        List<Stock> stocks = new ArrayList<>();
        Stock stock = null;
        try(Connection dbConnection = dataSourceDB.getConnection()){
            sqlRequest = "SELECT ingredient_move.id, type, ingredient_quantity, move_date FROM ingredient_move " +
                    "INNER JOIN ingredient ON ingredient.id = ingredient_move.ingredient_id " +
                    "WHERE ingredient_move.ingredient_id = ?";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setLong(1, ingredientId);
            ResultSet rs = select.executeQuery();
            Ingredient ingredient = ingredientDAO.findById(ingredientId);
            while(rs.next()){
                stock = new Stock(
                        rs.getLong("ingredient_move.id"),
                        ingredient,
                        MoveType.valueOf(rs.getString("type")),
                        rs.getTimestamp("move_date").toLocalDateTime()
                );
                stocks.add(stock);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return stocks;
    }
}
