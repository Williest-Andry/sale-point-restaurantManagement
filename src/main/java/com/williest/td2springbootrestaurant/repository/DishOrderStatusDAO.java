package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.model.DishOrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
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
        throw new UnsupportedOperationException("Not supported yet.");
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

