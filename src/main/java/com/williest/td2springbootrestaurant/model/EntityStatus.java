package com.williest.td2springbootrestaurant.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class EntityStatus {
    private Long id;
    private Status status;
    private LocalDateTime statusDate;
}
