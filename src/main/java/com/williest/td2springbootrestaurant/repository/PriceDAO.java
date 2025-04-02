package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.entity.Ingredient;
import com.williest.td2springbootrestaurant.entity.Price;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PriceDAO {
    private final DataSourceDB dataSourceDB;
    private String sqlRequest;
    private IngredientDAO ingredientDAO;

    public PriceDAO(DataSourceDB dataSourceDB) {
        this.dataSourceDB = dataSourceDB;
    }

    public List<Price> findAllByIngredientId(long ingredientId){
        List<Price> prices = new ArrayList<>();
        Price price = null;

        try(Connection dbConnection = dataSourceDB.getConnection()){
            sqlRequest = "SELECT id, amount, begin_date FROM price " +
                    "INNER JOIN ingredient ON ingredient.id = price.ingredient_id WHERE ingredient_id = ?";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setLong(1, ingredientId);
            ResultSet rs = select.executeQuery();
            Ingredient ingredient = ingredientDAO.findById(ingredientId);
            while(rs.next()){
                price = new Price(
                        rs.getLong("id"),
                        ingredient,
                        rs.getDouble("amount"),
                        rs.getTimestamp("begin_date").toLocalDateTime()
                );
                prices.add(price);
            }
        }
        catch(SQLException e){
            throw new RuntimeException("CAN'T FIND ALL PRICES OF THE INGREDIENT: ", e);
        }
        return prices;
    }

    public Price findByIngredientIdAndPriceDate(long ingredientId, LocalDateTime priceBeginDate){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
