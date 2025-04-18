package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.model.Dish;
import com.williest.td2springbootrestaurant.model.Sale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SaleDAO {
    private final DataSourceDB dataSourceDB;
    private String sqlRequest;

    public List<Sale> findAll(){
        List<Sale> sales = new ArrayList<>();

        try(Connection dbConnection = dataSourceDB.getConnection()){
            sqlRequest = "SELECT order_status.id AS sale_id, dish.id AS dish_id, dish.name, COUNT(order_status) AS total_sales FROM order_status " +
                    "JOIN dish_order ON dish_order.id=order_status.order_id " +
                    "JOIN dish ON dish.id=dish_order.dish_id " +
                    "WHERE order_status = 'SERVED' " +
                    "GROUP BY name, sale_id, dish.id " +
                    "ORDER BY total_sales DESC;";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            ResultSet rs = select.executeQuery();
            while(rs.next()){
                Sale sale = new Sale();
                sale.setId(rs.getLong("sale_id"));
                sale.setQuantitySold(rs.getInt("total_sales"));

                Dish dish = new Dish();
                dish.setId(rs.getLong("dish_id"));
                dish.setName(rs.getString("name"));
                sale.setDish(dish);

                sales.add(sale);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return sales;
    }
}
