package com.williest.td2springbootrestaurant.restController.mapper;

import com.williest.td2springbootrestaurant.model.EntityStatus;
import com.williest.td2springbootrestaurant.restController.rest.CreateStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Function;

@Component
public class StatusRestMapper implements Function<CreateStatus, EntityStatus> {
    @Override
    public EntityStatus apply(CreateStatus createStatus) {
        EntityStatus entityStatus = new EntityStatus();
        entityStatus.setStatus(createStatus.getStatus());
        entityStatus.setStatusDate(LocalDateTime.now());
        return entityStatus;
    }
}
