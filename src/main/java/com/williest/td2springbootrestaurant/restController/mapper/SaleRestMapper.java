package com.williest.td2springbootrestaurant.restController.mapper;

import com.williest.td2springbootrestaurant.model.Sale;
import com.williest.td2springbootrestaurant.restController.rest.SaleRest;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class SaleRestMapper implements Function<Sale, SaleRest> {
    @Override
    public SaleRest apply(Sale sale) {
        return new SaleRest(
                sale.getDish().getId(),
                sale.getDish().getName(),
                sale.getQuantitySold()
        );
    }
}