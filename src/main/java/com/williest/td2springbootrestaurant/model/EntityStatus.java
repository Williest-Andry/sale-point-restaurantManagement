package com.williest.td2springbootrestaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EntityStatus {
    @JsonIgnore
    private Long id;
    private Status status;
    private LocalDateTime statusDate = LocalDateTime.now();
}
