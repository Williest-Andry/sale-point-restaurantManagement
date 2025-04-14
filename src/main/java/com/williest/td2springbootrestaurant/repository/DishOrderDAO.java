package com.williest.td2springbootrestaurant.repository;

import com.williest.td2springbootrestaurant.model.DishOrder;
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
public class DishOrderDAO implements MakingOrderDAO<DishOrder>{
    private final DataSourceDB dataSource;
    private String sqlRequest;

    @Override
    public DishOrder findById(Long id) {
        DishOrder dishOrder = null;

        try(Connection dbConnection = dataSource.getConnection();) {
            sqlRequest = "SELECT id, order_id, dish_id, dish_quantity, dish_creation_date FROM dish_order WHERE id = ?;";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setLong(1, id);
            ResultSet rs = select.executeQuery();

            if(rs.next()) {
                dishOrder = new DishOrder();
                dishOrder.setId(rs.getLong("id"));
                dishOrder.setDishQuantity(rs.getInt("dish_quantity"));
                dishOrder.setDishOrderCreationDate(rs.getTimestamp("dish_creation_date").toLocalDateTime());
//                dishOrder.setDish(this.dishDAO.findById(rs.getLong("dish_id")));
//                dishOrder.setOrder(this.orderDAO.findById(rs.getLong("order_id")));
            }
        }
        catch(SQLException e) {
            throw new RuntimeException("ERROR IN FIND DishOrder BY ID : ",e);
        }
        return dishOrder;
    }

    @Override
    public List<DishOrder> findAll() {
        List<DishOrder> dishOrders = new ArrayList<>();
        DishOrder dishOrder = null;

        try(Connection dbConnection = dataSource.getConnection();) {
            sqlRequest = "SELECT id, order_id, dish_id, dish_quantity, dish_creation_date FROM dish_order;";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            ResultSet rs = select.executeQuery();

            while(rs.next()) {
                dishOrder = new DishOrder();
                dishOrder.setId(rs.getLong("id"));
                dishOrder.setDishQuantity(rs.getInt("dish_quantity"));
                dishOrder.setDishOrderCreationDate(rs.getTimestamp("dish_creation_date").toLocalDateTime());
//                dishOrder.setDish(this.dishDAO.findById(rs.getLong("dish_id")));
//                dishOrder.setOrder(this.orderDAO.findById(rs.getLong("order_id")));
                dishOrders.add(dishOrder);
            }
        }
        catch(SQLException e) {
            throw new RuntimeException("ERROR IN FIND ALL DishOrders : ",e);
        }
        return dishOrders;
    }

    public List<DishOrder> findAllByOrderId(Long id){
        List<DishOrder> dishOrders = new ArrayList<>();

        try(Connection dbConnection = dataSource.getConnection()){
            sqlRequest = "SELECT * FROM dish_order WHERE order_id = ?;";
            PreparedStatement select = dbConnection.prepareStatement(sqlRequest);
            select.setLong(1, id);
            ResultSet rs = select.executeQuery();
            while(rs.next()) {
                DishOrder dishOrder = new DishOrder();
                dishOrder.setId(rs.getLong("id"));
                dishOrder.setDishQuantity(rs.getInt("dish_quantity"));
                dishOrder.setDishOrderCreationDate(rs.getTimestamp("dish_creation_date").toLocalDateTime());
                dishOrders.add(dishOrder);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return dishOrders;
    }

    @Override
    public DishOrder save(DishOrder dishOrder){
        if(this.findById(dishOrder.getId()) == null){
            this.create(dishOrder);
        }
        else{
            try(Connection dbConnection = dataSource.getConnection();){
                sqlRequest = "UPDATE dish_order SET dish_quantity=?, dish_creation_date=? WHERE id=?";
                PreparedStatement update = dbConnection.prepareStatement(sqlRequest);
                update.setLong(1, dishOrder.getDishQuantity());
                update.setTimestamp(2, Timestamp.valueOf(dishOrder.getDishOrderCreationDate()));
                update.setLong(3, dishOrder.getId());
                update.executeUpdate();
            }
            catch(SQLException e){
                throw new RuntimeException("ERROR IN SAVE DishOrder : ",e);
            }
        }
        return this.findById(dishOrder.getId());
    }

    @Override
    public List<DishOrder> saveAll(List<DishOrder> dishOrders) {
        return dishOrders.stream().map(this::save).toList();
    }

    @Override
    public DishOrder create(DishOrder dishOrder) {
        Long dishOrderId = 0L;
        DishOrderStatus dishOrderStatus = null;
        if(dishOrder.getOrder().getActualStatus().getStatus() == Status.CREATED){
            try (Connection dbConnection = dataSource.getConnection();) {
                sqlRequest = "INSERT INTO dish_order (order_id, dish_id, dish_quantity, dish_creation_date) " +
                        "VALUES (?,?,?,?) RETURNING id;";
                PreparedStatement insert = dbConnection.prepareStatement(sqlRequest);
                insert.setLong(1, dishOrder.getOrder().getId());
                insert.setLong(2, dishOrder.getDish().getId());
                insert.setInt(3, dishOrder.getDishQuantity());
                insert.setTimestamp(4, Timestamp.valueOf(dishOrder.getDishOrderCreationDate() != null?
                        dishOrder.getDishOrderCreationDate() : LocalDateTime.now()));
                ResultSet rs = insert.executeQuery();
                if(rs.next()) {
                    dishOrderStatus = new DishOrderStatus();
                    dishOrderId = rs.getLong("id");
                    DishOrder dishOrderWithId = new DishOrder();
                    dishOrderWithId.setId(dishOrderId);
                    dishOrderStatus.setDishOrder(dishOrderWithId);
                }
                dishOrderStatus.setStatus(Status.CREATED);
                dishOrderStatus.setStatusDate(dishOrder.getDishOrderCreationDate() != null?
                        dishOrder.getDishOrderCreationDate() : LocalDateTime.now());
//                this.dishOrderStatusDAO.saveDishOrderStatus(dishOrderStatus);
            }
            catch(SQLException e) {
                throw new RuntimeException("ERROR IN SAVE DishOrder : ",e);
            }
            return dishOrder;
        }
        else{
            throw new RuntimeException("CAN'T CREATE dishOrder BECAUSE ORDER STATUS IS : " + dishOrder.getActualStatus());
        }
    }
}

