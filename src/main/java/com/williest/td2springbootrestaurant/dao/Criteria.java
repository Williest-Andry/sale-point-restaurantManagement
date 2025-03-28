package com.williest.td2springbootrestaurant.dao;

import java.util.Objects;

public class Criteria {
    private SQLOrder order;
    private FilterByThis filter;
    private String filterValue;
    private CriteriaType realCriteria;
    private String orderValue;

    public Criteria (SQLOrder order, String orderValue){
        this.order = order;
        this.realCriteria = order;
        this.orderValue = orderValue;
        this.filter = null;
        this.filterValue = null;
    }

    public Criteria (FilterByThis filter, String filterValue){
        this.filter = filter;
        this.realCriteria = filter;
        this.filterValue = filterValue;
        this.order = null;
        this.orderValue = null;
    }

    public CriteriaType getRealCriteria(){
        return realCriteria;
    }

    public String getFilterValue(){
        return filterValue;
    }

    public String getOrderValue(){
        return orderValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Criteria criteria = (Criteria) o;
        return order == criteria.order && filter == criteria.filter && Objects.equals(filterValue, criteria.filterValue) && Objects.equals(realCriteria, criteria.realCriteria) && Objects.equals(orderValue, criteria.orderValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, filter, filterValue, realCriteria, orderValue);
    }

    @Override
    public String toString() {
        return "Criteria{" +
                "order=" + order +
                ", filter=" + filter +
                ", filterValue='" + filterValue + '\'' +
                ", realCriteria=" + realCriteria +
                ", orderValue='" + orderValue + '\'' +
                '}';
    }
}

