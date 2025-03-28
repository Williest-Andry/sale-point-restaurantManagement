package com.williest.td2springbootrestaurant.dao;

import java.time.LocalDateTime;
import java.util.List;

public interface EntityDAO<T> {
    T findById(long id, LocalDateTime priceBeginDate, LocalDateTime storageStateDate);

    List<T> findAll(int page, int size);

    T save(T entity);

    T update(T entity);

    T deleteById(String id);
}

