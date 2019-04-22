package ru.softwerke.practice.app2019.query;

import ru.softwerke.practice.app2019.model.Entity;

import java.util.Objects;

public class QueryCondition<T extends Entity, R extends Comparable<? super R>> implements Condition<T> {
    private final QueryProperty<T, R> queryProperty;
    private final R queryValueTerm;
    
    public QueryCondition(QueryProperty<T, R> queryProperty, R queryValueTerm) {
        this.queryProperty = queryProperty;
        this.queryValueTerm = queryValueTerm;
    }
    
    public QueryProperty<T, R> getQueryProperty() {
        return queryProperty;
    }
    
    public R getQueryValueTerm() {
        return queryValueTerm;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryCondition<?, ?> that = (QueryCondition<?, ?>) o;
        return Objects.equals(queryProperty, that.queryProperty) &&
                Objects.equals(queryValueTerm, that.queryValueTerm);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(queryProperty, queryValueTerm);
    }
    
    @Override
    public boolean test(T entity) {
        R value = queryProperty.getFunction().apply(entity);
        int cmp = value.compareTo(queryValueTerm);
        switch (queryProperty.getOperator()) {
            case NOT_EQ:
                return cmp != 0;
            case EQ:
                return cmp == 0;
            case GREATER:
                return cmp > 0;
            case LESS:
                return cmp < 0;
            case LESS_OR_EQ:
                return cmp <= 0;
            case GREATER_OR_EQ:
                return cmp >= 0;
        }
        return false;
    }
}
