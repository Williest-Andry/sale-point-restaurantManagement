package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.entity.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Repository
public class IngredientDAO {
    private final DataSourceDB dataSourceDB;
    private final PriceDAO priceDAO;
    private final StockDAO stockDAO;

    public Ingredient findById(long id) {
        Ingredient ingredient = null;
        String sqlRequest = "SELECT ingredient.id, name, latest_modification, unit FROM ingredient WHERE id = ?; ";
        try (Connection dbConnection = dataSourceDB.getConnection();
             PreparedStatement selectIngredient = dbConnection.prepareStatement(sqlRequest);) {
            selectIngredient.setLong(1, id);
            try (ResultSet rs = selectIngredient.executeQuery()) {
                if (rs.next()) {
                    ingredient = new Ingredient(
                            rs.getString("name"),
                            rs.getTimestamp("latest_modification").toLocalDateTime(),
                            Unit.valueOf(rs.getString("unit"))
                    );
                    ingredient.setId(rs.getInt("id"));
                    ingredient.setPrices(priceDAO.findAllByIngredientId(id));
                    ingredient.setStocks(stockDAO.findAllByIngredientId(id));
                } else {
                    throw new RuntimeException("Ingredient with id: " + id + " doesn't exist!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredient;
    }

    public List<Ingredient> findAll(int page, int size) {
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient = null;
        String sqlRequest = "SELECT id, name, latest_modification, unit FROM ingredient; ";
        try (Connection dbConnection = dataSourceDB.getConnection();
             PreparedStatement selectIngredient = dbConnection.prepareStatement(sqlRequest);) {
            ResultSet rs = selectIngredient.executeQuery();
            while (rs.next()) {
                ingredient = new Ingredient(
                        rs.getString("name"),
                        rs.getTimestamp("latest_modification").toLocalDateTime(),
                        Unit.valueOf(rs.getString("unit"))
                );
                long ingredientId = rs.getInt("id");
                ingredient.setId(ingredientId);
                ingredient.setPrices(priceDAO.findAllByIngredientId(ingredientId));
                ingredient.setStocks(stockDAO.findAllByIngredientId(ingredientId));
                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR IN FIND INGREDIENT BY ID : " + e);
        }
        return ingredients;
    }

    public Ingredient save(Ingredient entity) {
        throw new RuntimeException("not implemented");
    }

    public Ingredient update(Ingredient entity) {
        return null;
    }

    public Ingredient deleteById(String id) {
        return null;
    }

    public List<Ingredient> findByCriteria(List<Criteria> criterias, List<LocalDateTime> latestModificationRange,
                                           List<Double> priceRange, int page, int size) {
        List<Ingredient> ingredients = new ArrayList<>();
        List<Criteria> orderCriteria = new ArrayList<>();
        String sqlRequestSelect = "SELECT ingredient_id as id, name, latest_modification, unit, amount as unit_price FROM ingredient " +
                "INNER JOIN price ON price.ingredient_id = ingredient.id ";
        String sqlRequestCondition = "WHERE 1=1 ";

        criterias.stream().filter(criteria -> {
            List<Criteria> ctr = new ArrayList<>();
            if (criteria.getRealCriteria() == SQLOrder.ASC) {
                ctr.add(criteria);
                return true;
            } else if (criteria.getRealCriteria() == SQLOrder.DESC) {
                ctr.add(criteria);
                return true;
            }
            return false;
        }).forEach(criteria -> orderCriteria.add(criteria));

        for (Criteria criteria : criterias) {
            if (criteria.getRealCriteria() == FilterByThis.NAME) {
                sqlRequestCondition += " AND " + criteria.getRealCriteria().toString().toLowerCase() + " ILIKE " + "'%" + criteria.getFilterValue().toUpperCase() + "%'";
            }
            if (criteria.getRealCriteria() == FilterByThis.UNIT) {
                sqlRequestCondition += " AND " + criteria.getRealCriteria().toString().toLowerCase() + "=" + "'" + criteria.getFilterValue().toUpperCase() + "'";
            }
        }

        if (latestModificationRange == null) {
            throw new RuntimeException("latestModificationRange is null");
        } else if (latestModificationRange.isEmpty()) {
            sqlRequestCondition += "";
        } else if (latestModificationRange.size() == 1) {
            sqlRequestCondition += " AND latest_modification = ?";
        } else {
            Collections.sort(latestModificationRange);
            for (int i = 0; i < 2; i++) {
                if (latestModificationRange.get(i) != null && latestModificationRange.getFirst() == latestModificationRange.get(i)) {
                    sqlRequestCondition += " AND latest_modification > ?";
                } else {
                    sqlRequestCondition += " AND latest_modification < ?";
                }
            }
        }

        if (priceRange == null) {
            throw new RuntimeException("price range is null");
        } else if (priceRange.isEmpty()) {
            sqlRequestCondition += "";
        } else if (priceRange.size() == 1) {
            sqlRequestCondition += " AND price = ?";
        } else {
            Collections.sort(priceRange);
            for (Double price : priceRange) {
                if (price != null && priceRange.getFirst() == price) {
                    sqlRequestCondition += " AND amount > " + price;
                }
                if (price != null && priceRange.getLast() == price) {
                    sqlRequestCondition += " AND amount < " + price;
                }
            }
        }

        try (Connection dbConnection = dataSourceDB.getConnection()) {
            String sqlRequest = sqlRequestSelect + sqlRequestCondition + " ORDER BY ? LIMIT ? OFFSET ?;";
            PreparedStatement selectIngredients = dbConnection.prepareStatement(sqlRequest);
            selectIngredients.setTimestamp(1, Timestamp.valueOf(latestModificationRange.get(0)));
            selectIngredients.setTimestamp(2, Timestamp.valueOf(latestModificationRange.get(1)));
            selectIngredients.setString(3, orderCriteria.getFirst().getRealCriteria().toString() + " " + orderCriteria.getFirst().getOrderValue());
            selectIngredients.setInt(4, size);
            selectIngredients.setInt(5, size * page - size);

            ResultSet rs = selectIngredients.executeQuery();
            while (rs.next()) {
                Ingredient ingredient = new Ingredient(
                        rs.getString("name"),
                        rs.getTimestamp("latest_modification").toLocalDateTime(),
                        Unit.valueOf(rs.getString("unit"))
                );
                ingredient.setId(rs.getInt("id"));
                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR IN FIND BY Criteria : " + e);
        }

        return ingredients;
    }
}
