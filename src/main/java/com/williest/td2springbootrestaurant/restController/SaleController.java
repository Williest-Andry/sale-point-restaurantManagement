package com.williest.td2springbootrestaurant.restController;

import com.williest.td2springbootrestaurant.restController.mapper.SaleRestMapper;
import com.williest.td2springbootrestaurant.restController.rest.SaleRest;
import com.williest.td2springbootrestaurant.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SaleController {
    private final SaleService saleService;
    private final SaleRestMapper saleRestMapper;

    @GetMapping("/sales")
    public ResponseEntity<Object> getSales() {
        List<SaleRest> sales = this.saleService.getAll().stream().map(this.saleRestMapper::apply).toList();
        return ResponseEntity.ok().body(sales);
    }
}
