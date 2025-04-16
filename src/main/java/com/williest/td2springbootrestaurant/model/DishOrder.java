package com.williest.td2springbootrestaurant.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
public class DishOrder{
    private Long id;
    private Dish dish;
    private int dishQuantity;
    private Order order;
    private LocalDateTime dishOrderCreationDate;
    private List<DishOrderStatus> dishOrderStatus = new ArrayList<>();

    public void addAndUpdateStatus(DishOrderStatus newDishOrderStatus) {
        Status actualStatus = this.getActualStatus()!=null ? this.getActualStatus().getStatus() : null;
        Status newStatus = newDishOrderStatus.getStatus();
        switch (actualStatus){
            case Status.CREATED -> {
                switch (newStatus) {
                    case Status.CONFIRMED -> this.dishOrderStatus.add(newDishOrderStatus);
                    default -> throw new RuntimeException("CAN'T UPDATE BECAUSE ACTUAL STATUS IS: " + actualStatus);
                }
            }
            case Status.CONFIRMED -> {
                switch (newStatus) {
                    case Status.IN_PREPARATION -> this.dishOrderStatus.add(newDishOrderStatus);
                    default -> throw new RuntimeException("CAN'T UPDATE ACTUAL STATUS IS: " + actualStatus);
                }
            }
            case Status.IN_PREPARATION -> {
                switch (newStatus) {
                    case Status.DONE-> this.dishOrderStatus.add(newDishOrderStatus);
                    default -> throw new RuntimeException("CAN'T UPDATE ACTUAL STATUS IS: " + actualStatus);
                }
            }
            case Status.DONE -> {
                switch (newStatus) {
                    case Status.SERVED-> this.dishOrderStatus.add(newDishOrderStatus);
                    default -> throw new RuntimeException("CAN'T UPDATE ACTUAL STATUS IS: " + actualStatus);
                }
            }
            case Status.SERVED ->{
                switch (newStatus) {
                    default -> throw new RuntimeException("CAN'T UPDATE ACTUAL STATUS IS: " + actualStatus);
                }
            }
            case null, default -> {
                switch (newStatus) {
                    case Status.CREATED -> this.dishOrderStatus.add(newDishOrderStatus);
                    default -> throw new RuntimeException("CAN'T UPDATE ACTUAL STATUS IS: " + actualStatus);
                }
            }
        }
    }

    public DishOrderStatus getActualStatus(){
        DishOrderStatus defaultStatus = new DishOrderStatus();
        defaultStatus.setStatus(Status.CREATED);
        return dishOrderStatus.stream().max(Comparator.comparing(DishOrderStatus::getStatusDate))
                .orElse(defaultStatus);
    }
}

