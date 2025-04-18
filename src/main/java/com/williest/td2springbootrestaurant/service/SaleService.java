package com.williest.td2springbootrestaurant.service;

import com.williest.td2springbootrestaurant.model.Sale;
import com.williest.td2springbootrestaurant.repository.SaleDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleDAO saleDAO;

    public List<Sale> getAll(){
        return this.saleDAO.findAll();
    }
}
