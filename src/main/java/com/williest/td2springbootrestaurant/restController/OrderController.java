package com.williest.td2springbootrestaurant.restController;

import com.williest.td2springbootrestaurant.model.Order;
import com.williest.td2springbootrestaurant.restController.mapper.OrderRestMapper;
import com.williest.td2springbootrestaurant.restController.mapper.StatusRestMapper;
import com.williest.td2springbootrestaurant.restController.rest.CreateOrder;
import com.williest.td2springbootrestaurant.restController.rest.CreateStatus;
import com.williest.td2springbootrestaurant.restController.rest.OrderRest;
import com.williest.td2springbootrestaurant.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderRestMapper orderRestMapper;
    private final StatusRestMapper statusRestMapper;

    @GetMapping("/orders/{reference}")
    public ResponseEntity<Object> getOrderByReference(@PathVariable Long reference) {
        try{
            OrderRest orderRest = orderRestMapper.apply(orderService.getOrderByReference(reference));
            return ResponseEntity.ok().body(orderRest);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }
    }

    @PutMapping("/orders/{reference}/dishes")
    public ResponseEntity<Object> updateOrderDishByReference(@PathVariable Long reference,
                                                             @RequestBody CreateOrder createOrder){
        Order order = orderRestMapper.toModel(createOrder);
        OrderRest orderRest = orderRestMapper.apply(orderService.updateDishOrders(reference, order.getDishOrders()));
        return ResponseEntity.ok().body(orderRest);
    }

    @PutMapping("/orders/{reference}/dishes/{dishId}")
    public ResponseEntity<Object> updateOrderDishStatusByReference(@PathVariable Long reference,
                                                                   @PathVariable Long dishId,
                                                                   @RequestBody CreateStatus createStatus){

        Order order = orderService.updateOrderDishStatusById(reference, dishId, this.statusRestMapper.toModel(createStatus).getStatus());
        return ResponseEntity.ok().body(orderRestMapper.apply(order));
    }
}
