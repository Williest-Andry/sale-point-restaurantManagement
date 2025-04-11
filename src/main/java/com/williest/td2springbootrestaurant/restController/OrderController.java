package com.williest.td2springbootrestaurant.restController;

import com.williest.td2springbootrestaurant.restController.mapper.OrderRestMapper;
import com.williest.td2springbootrestaurant.restController.rest.OrderRest;
import com.williest.td2springbootrestaurant.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderRestMapper orderRestMapper;

    @GetMapping("/orders/{reference}")
    public ResponseEntity<Object> getOrderByReference(@PathVariable Long reference) {
        try{
            OrderRest orderRest = orderRestMapper.apply(orderService.getOrderByReference(reference));
            return ResponseEntity.ok().body(orderRest);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }
    }
}
