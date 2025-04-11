package com.williest.td2springbootrestaurant.repository;

import java.util.List;

public interface MakingOrderDAO<T>{
    T findById(Long id);

    List<T> findAll();

    T create(T orderEntity);

    T save(T orderEntity);

    List<T> saveAll(List<T> orderEntity);
}

