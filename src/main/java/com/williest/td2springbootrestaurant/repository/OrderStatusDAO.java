package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.model.DishOrderStatus;
import com.williest.td2springbootrestaurant.model.OrderStatus;
import com.williest.td2springbootrestaurant.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderStatusDAO {
    private final DataSourceDB dataSourceDB;
    private String sqlRequest;

    public OrderStatus findById(Long id) {
        OrderStatus orderStatus = null;
        try(Connection dbConnection = dataSourceDB.getConnection()){
            sqlRequest = "SELECT * FROM order_status WHERE id = ?";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setLong(1, id);
            ResultSet rs = select.executeQuery();
            if(rs.next()){
                orderStatus = new OrderStatus();
                orderStatus.setId(id);
                orderStatus.setStatus(Status.valueOf(rs.getString("order_status")));
                orderStatus.setStatusDate(rs.getTimestamp("order_status_date").toLocalDateTime());
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return orderStatus;
    }

    public List<OrderStatus> findAllByOrderId(long id) {
        List<OrderStatus> orderStatus = new ArrayList<>();

        try(Connection dbConnection = dataSourceDB.getConnection()){
            sqlRequest = "SELECT id AS status_id, order_id, order_status, order_status_date FROM order_status WHERE order_id = ?";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setLong(1, id);
            ResultSet rs = select.executeQuery();
            while(rs.next()){
                OrderStatus status = new OrderStatus();
                status.setId(rs.getLong("status_id"));
                status.setStatus(Status.valueOf(rs.getString("order_status")));
                status.setStatusDate(rs.getTimestamp("order_status_date").toLocalDateTime());
                orderStatus.add(status);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return orderStatus;
    }

    public OrderStatus saveOrderStatus(OrderStatus orderStatus) {
        if(orderStatus.getId() != null){
            return this.update(orderStatus);
        }
        Long orderStatusId = 0L;
        try (Connection dbConnection = dataSourceDB.getConnection()){
            sqlRequest = "INSERT INTO order_status (order_id, order_status, order_status_date) " +
                    " VALUES (?,?::status,?) RETURNING id;";
            PreparedStatement insert = dbConnection.prepareStatement(sqlRequest);
            insert.setLong(1, orderStatus.getOrder().getId());
            insert.setString(2, orderStatus.getStatus().toString());
            insert.setTimestamp(3, orderStatus.getStatusDate() != null ? Timestamp.valueOf(orderStatus.getStatusDate()) :
                    Timestamp.valueOf(LocalDateTime.now()));
            ResultSet rs = insert.executeQuery();
            if(rs.next()){
                orderStatusId = rs.getLong("id");
            }
        }
        catch(SQLException e) {
            throw new RuntimeException("ERROR IN CREATE ORDER STATUS : ",e);
        }
        return this.findById(orderStatusId);
    }

    public OrderStatus update(OrderStatus orderStatus) {
        try(Connection dbConnection = dataSourceDB.getConnection()){
            sqlRequest = "UPDATE order_status SET order_status_date = ? WHERE id = ?";
            PreparedStatement update = dbConnection.prepareStatement(sqlRequest);
            update.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            update.setLong(2, orderStatus.getId());
            update.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return this.findById(orderStatus.getId());
    }
}
