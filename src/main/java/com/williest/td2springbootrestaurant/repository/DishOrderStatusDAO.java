package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.model.DishOrderStatus;
import com.williest.td2springbootrestaurant.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishOrderStatusDAO {
    private final DataSourceDB dataSource;
    private String sqlRequest;

    public DishOrderStatus findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<DishOrderStatus> findAllByDishOrderId(Long dishOrderId) {
        List<DishOrderStatus> dishOrderStatusList = new ArrayList<>();
        try(Connection dbConnection = dataSource.getConnection()){
            sqlRequest = "SELECT * FROM dish_order_status WHERE dish_order_id = ?";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setLong(1, dishOrderId);
            ResultSet rs = select.executeQuery();
            while(rs.next()){
                DishOrderStatus dishOrderStatus = new DishOrderStatus();
                dishOrderStatus.setId(rs.getLong("id"));
                dishOrderStatus.setStatus(Status.valueOf(rs.getString("dish_order_status")));
                dishOrderStatus.setStatusDate(rs.getTimestamp("dish_order_status_date").toLocalDateTime());

                dishOrderStatusList.add(dishOrderStatus);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return dishOrderStatusList;
    }

    public DishOrderStatus saveDishOrderStatus(DishOrderStatus dishOrderStatus){
        Long dishOrderStatusId = 0L;
        try (Connection dbConnection = dataSource.getConnection()){
            sqlRequest = "INSERT INTO dish_order_status (dish_order_id, dish_order_status, dish_order_status_date) " +
                    "VALUES (?,?::status,?) RETURNING id;";
            PreparedStatement insert = dbConnection.prepareStatement(sqlRequest);
            insert.setLong(1, dishOrderStatus.getDishOrder().getId());
            insert.setString(2, dishOrderStatus.getStatus().toString());
            insert.setTimestamp(3, dishOrderStatus.getStatusDate() != null ? Timestamp.valueOf(dishOrderStatus.getStatusDate()) :
                    Timestamp.valueOf(LocalDateTime.now()));
            ResultSet rs = insert.executeQuery();
            dishOrderStatusId = rs.getLong("id");
        }
        catch(SQLException e) {
            throw new RuntimeException("ERROR IN CREATE DISH ORDER STATUS : ",e);
        }
        return this.findById(dishOrderStatusId);
    }

    public List<DishOrderStatus> saveAll(List<DishOrderStatus> dishOrderStatusList){
        return dishOrderStatusList.stream().map(this::saveDishOrderStatus).toList();
    }
}

