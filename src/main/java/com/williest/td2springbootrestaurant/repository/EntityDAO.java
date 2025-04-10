package com.williest.td2springbootrestaurant.repository;

import java.time.LocalDateTime;
import java.util.List;

public interface EntityDAO<T> {
    T findById(long id);

    List<T> findAll(int page, int size);

    T save(T entity);

    List<T> saveAll(List<T> entities);

    T update(T entity);

    T deleteById(String id);
}

