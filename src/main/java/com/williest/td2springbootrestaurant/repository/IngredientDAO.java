package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.entity.Ingredient;
import com.williest.td2springbootrestaurant.entity.Unit;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class IngredientDAO implements EntityDAO<Ingredient> {
    private final CustomDataSource customDataSource;

    public IngredientDAO(CustomDataSource customDataSource) {
        this.customDataSource = customDataSource;
    }

    public Double getTotalUsedIngredient (long id , LocalDateTime storageStateDate) {
        Double totalUsedIngredient = 0.0;
        String sqlRequest = "SELECT SUM(ingredient_quantity) as sortie_quantity FROM ingredient" +
                " INNER JOIN ingredient_move ON ingredient.id = ingredient_move.ingredient_id " +
                " WHERE ingredient.id = ? AND move_date <= ? AND type = 'sortie';";

        try (Connection dbConnection = customDataSource.getConnection()) {
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setLong(1, id);
            select.setTimestamp(2, Timestamp.valueOf(storageStateDate != null ? storageStateDate : LocalDateTime.now()));
            ResultSet rs = select.executeQuery();

            if (rs.next()) {
                totalUsedIngredient = rs.getDouble("sortie_quantity");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("ERROR IN GET USED INGREDIENT", e);
        }

        return totalUsedIngredient;
    }

    @Override
    public Ingredient findById(long id , LocalDateTime priceBeginDate, LocalDateTime storageStateDate) {
        Ingredient ingredient = null;
        String sqlRequest = new StringBuilder()
                .append("SELECT ingredient.id, name, latest_modification, amount as unit_price, ingredient.unit, ")
                .append("SUM(ingredient_quantity) as entree_quantity FROM ingredient ")
                .append("INNER JOIN price ON price.ingredient_id = ingredient.id ")
                .append("INNER JOIN ingredient_move ON ingredient_move.ingredient_id = ingredient.id ")
                .append("WHERE ingredient.id = ? AND move_date <= ? AND type = 'entree' ").toString();

        if (priceBeginDate != null) {
            sqlRequest += "AND price.begin_date = ? ";
        }

        sqlRequest += "GROUP BY ingredient.id, name, latest_modification, amount, ingredient.unit, price.begin_date " +
                "ORDER BY price.begin_date DESC LIMIT 1";

        try (Connection dbConnection = customDataSource.getConnection();
             PreparedStatement selectIngredient = dbConnection.prepareStatement(sqlRequest);) {
            selectIngredient.setLong(1, id);
            selectIngredient.setTimestamp(2, Timestamp.valueOf(storageStateDate != null ? storageStateDate : LocalDateTime.now()));

            if (priceBeginDate != null) {
                selectIngredient.setTimestamp(3, Timestamp.valueOf(priceBeginDate));
            }

            try (ResultSet rs = selectIngredient.executeQuery()) {
                if (rs.next()) {
                    ingredient = new Ingredient(
                            rs.getString("name"),
                            rs.getTimestamp("latest_modification").toLocalDateTime(),
                            rs.getDouble("unit_price"),
                            Unit.valueOf(rs.getString("unit"))
                    );
                    ingredient.setId(rs.getInt("id"));
                    ingredient.setStorageTotalIngredient(rs.getDouble("entree_quantity"));
                    ingredient.setUsedTotalIngredient(this.getTotalUsedIngredient(id, storageStateDate));
                }
                else{
                    throw new RuntimeException("ingredient with id: "+id+" doesn't exist!");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR IN FIND INGREDIENT BY ID : " + e);
        }
        return ingredient;
    }

    @Override
    public List<Ingredient> findAll(int page, int size) {
        List<Ingredient> ingredients = new ArrayList<>();
        String sqlRequest = new StringBuilder()
                .append("SELECT ingredient.id as ingredient_id, name, latest_modification, amount as unit_price, ingredient.unit, ")
                .append("SUM(ingredient_quantity) as entree_quantity FROM ingredient ")
                .append("INNER JOIN price ON price.ingredient_id = ingredient.id ")
                .append("INNER JOIN ingredient_move ON ingredient_move.ingredient_id = ingredient.id ")
                .append("WHERE move_date <= ? AND type = 'entree' AND price.begin_date <= ? ").toString();

        sqlRequest += " GROUP BY ingredient.id, name, latest_modification, amount, ingredient.unit, price.begin_date " +
                "ORDER BY price.begin_date;";

        try (Connection dbConnection = customDataSource.getConnection();
             PreparedStatement selectIngredient = dbConnection.prepareStatement(sqlRequest);) {
            selectIngredient.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            selectIngredient.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

            try (ResultSet rs = selectIngredient.executeQuery()) {
                while (rs.next()) {
                    Ingredient ingredient = new Ingredient(
                            rs.getString("name"),
                            rs.getTimestamp("latest_modification").toLocalDateTime(),
                            rs.getDouble("unit_price"),
                            Unit.valueOf(rs.getString("unit"))
                    );
                    ingredient.setId(rs.getInt("ingredient_id"));
                    ingredient.setStorageTotalIngredient(rs.getDouble("entree_quantity"));
                    ingredient.setUsedTotalIngredient(this.getTotalUsedIngredient(rs.getLong("ingredient_id"), null));
                    ingredients.add(ingredient);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR IN FIND ALL INGREDIENTS : " + e);
        }
        return ingredients;
    }

    @Override
    public Ingredient save(Ingredient entity) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Ingredient update(Ingredient entity) {
        return null;
    }

    @Override
    public Ingredient deleteById(String id) {
        return null;
    }

    public List<Ingredient> findByCriteria (List<Criteria> criterias, List<LocalDateTime> latestModificationRange,
                                            List<Double> priceRange, int page, int size) {
        List<Ingredient> ingredients = new ArrayList<>();
        List<Criteria> orderCriteria = new ArrayList<>();
        String sqlRequestSelect = "SELECT ingredient_id as id, name, latest_modification, unit, amount as unit_price FROM ingredient " +
                "INNER JOIN price ON price.ingredient_id = ingredient.id ";
        String sqlRequestCondition = "WHERE 1=1 ";

        criterias.stream().filter(criteria -> {
            List<Criteria> ctr = new ArrayList<>();
            if(criteria.getRealCriteria() == SQLOrder.ASC){
                ctr.add(criteria);
                return true;
            }
            else if(criteria.getRealCriteria() == SQLOrder.DESC){
                ctr.add(criteria);
                return true;
            }
            return false;
        }).forEach(criteria -> orderCriteria.add(criteria));

        for (Criteria criteria : criterias) {
            if(criteria.getRealCriteria() == FilterByThis.NAME) {
                sqlRequestCondition += " AND " + criteria.getRealCriteria().toString().toLowerCase() + " ILIKE " + "'%"+criteria.getFilterValue().toUpperCase()+"%'";
            }
            if(criteria.getRealCriteria() == FilterByThis.UNIT){
                sqlRequestCondition += " AND " + criteria.getRealCriteria().toString().toLowerCase() + "=" + "'"+criteria.getFilterValue().toUpperCase()+"'";
            }
        }

        if(latestModificationRange == null){
            throw new RuntimeException("latestModificationRange is null");
        }
        else if(latestModificationRange.isEmpty()){
            sqlRequestCondition += "";
        }
        else if (latestModificationRange.size() == 1){
            sqlRequestCondition += " AND latest_modification = ?";
        }
        else{
            Collections.sort(latestModificationRange);
            for (int i=0; i<2; i++) {
                if (latestModificationRange.get(i) != null  && latestModificationRange.getFirst() == latestModificationRange.get(i)){
                    sqlRequestCondition += " AND latest_modification > ?";
                }
                else {
                    sqlRequestCondition += " AND latest_modification < ?";
                }
            }
        }

        if (priceRange == null) {
            throw new RuntimeException("price range is null");
        }
        else if (priceRange.isEmpty()){
            sqlRequestCondition +="";
        }
        else if (priceRange.size() == 1){
            sqlRequestCondition += " AND price = ?";
        }
        else {
            Collections.sort(priceRange);
            for (Double price : priceRange) {
                if (price != null  && priceRange.getFirst() == price){
                    sqlRequestCondition += " AND amount > " + price;
                }
                if (price != null  && priceRange.getLast() == price){
                    sqlRequestCondition += " AND amount < " + price;
                }
            }
        }

        try (Connection dbConnection = customDataSource.getConnection()){
            String sqlRequest = sqlRequestSelect + sqlRequestCondition + " ORDER BY ? LIMIT ? OFFSET ?;";
            PreparedStatement selectIngredients = dbConnection.prepareStatement(sqlRequest);
            selectIngredients.setTimestamp(1, Timestamp.valueOf(latestModificationRange.get(0)));
            selectIngredients.setTimestamp(2, Timestamp.valueOf(latestModificationRange.get(1)));
            selectIngredients.setString(3, orderCriteria.getFirst().getRealCriteria().toString() + " "+ orderCriteria.getFirst().getOrderValue());
            selectIngredients.setInt(4, size);
            selectIngredients.setInt(5, size * page - size);

            ResultSet rs = selectIngredients.executeQuery();
            while (rs.next()) {
                Ingredient ingredient = new Ingredient(
                        rs.getString("name"),
                        rs.getTimestamp("latest_modification").toLocalDateTime(),
                        rs.getDouble("unit_price"),
                        Unit.valueOf(rs.getString("unit"))
                );
                ingredient.setId(rs.getInt("id"));
                ingredients.add(ingredient);
            }
        }
        catch(SQLException e){
            throw new RuntimeException("ERROR IN FIND BY Criteria : "+e);
        }

        return ingredients;
    }
}
