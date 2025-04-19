package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.model.Order;
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
public class OrderDAO implements MakingOrderDAO<Order>{
    private final DataSourceDB dataSourceDB ;
    private String sqlRequest;

    @Override
    public Order findById(Long id) {
        Order order = null;
        try (Connection dbConnection = dataSourceDB.getConnection();) {
            sqlRequest = "SELECT \"order\".id AS order_id, order_date, reference FROM \"order\" WHERE \"order\".id = ?;";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setLong(1, id);
            ResultSet rs = select.executeQuery();
            if (rs.next()) {
                order = new Order();
                order.setId(rs.getLong("order_id"));
                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                order.setReference(rs.getString("reference"));
//                order.setOrderStatus(orderStatusDAO.findAllByOrderId(id));
//                order.setDishOrders(dishOrderDAO.findAllByOrderId(id));
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR IN FIND ORDER BY ID : " + e);
        }
        return order;
    }

    public Order findByReference(String reference){
        Order order = null;
        try (Connection dbConnection = dataSourceDB.getConnection();) {
            sqlRequest = "SELECT \"order\".id AS order_id, order_date, reference FROM \"order\" WHERE \"order\".reference = ?;";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setString(1, reference);
            ResultSet rs = select.executeQuery();
            if (rs.next()) {
                order = new Order();
                order.setId(rs.getLong("order_id"));
                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                order.setReference(rs.getString("reference"));
//                order.setOrderStatus(orderStatusDAO.findAllByOrderId(id));
//                order.setDishOrders(dishOrderDAO.findAllByOrderId(id));
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR IN FIND ORDER BY REFERENCE : " + e);
        }
        return order;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        Order order = null;

        try (Connection dbConnection = dataSourceDB.getConnection();) {
            sqlRequest = "SELECT \"order\".id AS order_id, order_date, reference FROM \"order\";";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            ResultSet rs = select.executeQuery();
            while (rs.next()) {
                order = new Order();
                order.setId(rs.getLong("order_id"));
                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                order.setReference(rs.getString("reference"));
//                order.setOrderStatus(orderStatusDAO.findAllByOrderId(rs.getLong("order_id")));
//                order.setDishOrders(dishOrderDAO.findAllByOrderId(rs.getLong("order_id")));
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR IN FIND ALL ORDERS: " + e);
        }
        return orders;
    }

    public Order save(Order order) {
        if(this.findById(order.getId()) == null) {
            return this.create(order);
        }
        else{
            try(Connection dbConnection = dataSourceDB.getConnection()){
                sqlRequest = "UPDATE \"order\" SET order_date=? WHERE id=?;";
                PreparedStatement update = dbConnection.prepareStatement(sqlRequest);
                update.setTimestamp(1, Timestamp.valueOf(order.getOrderDate()));
                update.setLong(2, order.getId());
                update.executeUpdate();
//                order.getDishOrders().forEach(this.dishOrderDAO::save);
//                order.getOrderStatus().forEach(this.orderStatusDAO::saveOrderStatus);
            }
            catch(SQLException e) {
                throw new RuntimeException("ERROR IN SAVE ORDER: "+e);
            }
        }
        return this.findById(order.getId());
    }

    @Override
    public List<Order> saveAll(List<Order> orderEntity) {
        return List.of();
    }


    @Override
    public Order create(Order order) {
        Long orderId = 0L;
        try(Connection dbConnection = dataSourceDB.getConnection()){
            sqlRequest = "INSERT INTO \"order\" (order_date, reference) VALUES (?,?) RETURNING id;";
            PreparedStatement insert = dbConnection.prepareStatement(sqlRequest);
            insert.setTimestamp(1, Timestamp.valueOf(order.getOrderDate() != null?
                    order.getOrderDate() : LocalDateTime.now() ));
            insert.setString(2, order.getReference());
            ResultSet rs = insert.executeQuery();
            if(rs.next()) {
                orderId = rs.getLong("id");
            }
            if(order.getOrderStatus().isEmpty()){
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setStatus(Status.CREATED);
                orderStatus.setStatusDate(order.getOrderDate());
                Order orderWithId = new Order();
                orderWithId.setId(orderId);
                orderStatus.setOrder(orderWithId);
//                this.orderStatusDAO.saveOrderStatus(orderStatus);
            }
//            order.getDishOrders().forEach(this.dishOrderDAO::save);
        }
        catch(SQLException e){
            throw new RuntimeException("ERROR IN CREATE ORDER : " +e);
        }
        return this.findById(orderId);
    }
}
