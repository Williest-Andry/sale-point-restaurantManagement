package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.model.Ingredient;
import com.williest.td2springbootrestaurant.model.Price;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PriceDAO {
    private final DataSourceDB dataSourceDB;
    private String sqlRequest;
    private IngredientDAO ingredientDAO;

    public PriceDAO(DataSourceDB dataSourceDB, IngredientDAO ingredientDAO) {
        this.dataSourceDB = dataSourceDB;
        this.ingredientDAO = ingredientDAO;
    }

    public Price findById(Long id) {
        Price price = null;
        try(Connection dbConnection = dataSourceDB.getConnection()){
            sqlRequest = "SELECT id, ingredient_id, amount, begin_date FROM price WHERE id = ?";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setLong(1, id);
            ResultSet rs = select.executeQuery();
            if(rs.next()){
                price = new Price();
                price.setId(rs.getLong("id"));
                price.setBeginDate(rs.getTimestamp("begin_date").toLocalDateTime());
                price.setAmount(rs.getDouble("amount"));
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return price;
    }

    public List<Price> findAllByIngredientId(Long ingredientId){
        List<Price> prices = new ArrayList<>();
        Price price = null;

        try(Connection dbConnection = dataSourceDB.getConnection()){
            sqlRequest = "SELECT price.id as price_id, amount, begin_date FROM price " +
                    "INNER JOIN ingredient ON ingredient.id = price.ingredient_id WHERE ingredient_id = ?";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setLong(1, ingredientId);
            ResultSet rs = select.executeQuery();
            Ingredient ingredient = ingredientDAO.findById(ingredientId);
            while(rs.next()){
                price = new Price();
                price.setId(rs.getLong("price_id"));
                price.setIngredient(ingredient);
                price.setAmount(rs.getDouble("amount"));
                price.setBeginDate(rs.getTimestamp("begin_date").toLocalDateTime());
                prices.add(price);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return prices;
    }

    public Price findByIngredientIdAndPriceDate(long ingredientId, LocalDateTime priceBeginDate){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Price save(Price price) {
        Long priceId = 0L;
        if(price.getId() != null){
            return this.findById(price.getId());
        }
        try(Connection dbConnection = dataSourceDB.getConnection()){
            sqlRequest = "INSERT INTO price(ingredient_id, amount, begin_date) VALUES (?,?,?) RETURNING id;";
            PreparedStatement insert = dbConnection.prepareStatement(sqlRequest);
            insert.setLong(1, price.getIngredient().getId());
            insert.setDouble(2, price.getAmount());
            insert.setTimestamp(3, Timestamp.valueOf(price.getBeginDate()));
            ResultSet rs = insert.executeQuery();
            if(rs.next()){
                priceId= rs.getLong("id");
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return this.findById(priceId);
    }

    public List<Price> saveAll(List<Price> prices){
        List<Price> savedPrices = new ArrayList<>();
        for (Price price : prices) {
            Price savedPrice = this.save(price);
            savedPrices.add(savedPrice);
        }
        return savedPrices;
    }

    public Price update(Price price){
        throw new UnsupportedOperationException("not implemented yet");
    }
}
