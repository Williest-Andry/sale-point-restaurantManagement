package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.model.OrderStatus;
import com.williest.td2springbootrestaurant.model.Status;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderStatusDAO {
    private DataSourceDB dataSourceDB;
    private String sqlRequest;
    private OrderDAO orderDAO;

    public OrderStatus findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
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
                status.setOrder(this.orderDAO.findById(id));
                orderStatus.add(status);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return orderStatus;
    }

    public OrderStatus saveOrderStatus(OrderStatus orderStatus) {
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
            orderStatusId = rs.getLong("id");
        }
        catch(SQLException e) {
            throw new RuntimeException("ERROR IN CREATE ORDER STATUS : ",e);
        }
        return this.findById(orderStatusId);
    }
}
