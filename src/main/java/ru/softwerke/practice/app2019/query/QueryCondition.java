package ru.softwerke.practice.app2019.query;

import org.apache.commons.lang3.Validate;
import ru.softwerke.practice.app2019.model.Entity;

import java.util.Objects;
import java.util.function.Function;

import static ru.softwerke.practice.app2019.query.FilterOperator.CONTAINS;

public class QueryCondition<T extends Entity, R extends Comparable<? super R>> implements Condition<T> {
    private final Function<T, R> function;
    private final FilterOperator operator;
    private final R queryValueTerm;
    private final String queryPrefixTerm;
    
    QueryCondition(Function<T, R> function, FilterOperator operator, R queryValueTerm) {
        Validate.notNull(function, "function can't be null");
        Validate.notNull(operator, "operator can't be null");
        Validate.notNull(queryValueTerm, "queryValueTerm can't be null");
        this.function = function;
        this.operator = operator;
        this.queryValueTerm = queryValueTerm;
        this.queryPrefixTerm = null;
    }
    
    QueryCondition(Function<T, R> function, String queryPrefixTerm) {
        Validate.notNull(function, "function can't be null");
        Validate.notNull(queryPrefixTerm, "queryPrefixTerm can't be null");
        this.function = function;
        this.queryPrefixTerm = queryPrefixTerm;
        this.operator = null;
        this.queryValueTerm = null;
    }
    
    QueryCondition(Function<T, R> function, FilterOperator operator) {
        Validate.notNull(function, "function can't be null");
        this.function = function;
        this.operator = operator;
        this.queryPrefixTerm = null;
        this.queryValueTerm = null;
    }
    
    public Function<T, R> getFunction() {
        return function;
    }
    
    public FilterOperator getOperator() {
        return operator;
    }
    
    public R getQueryValueTerm() {
        return queryValueTerm;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryCondition<?, ?> that = (QueryCondition<?, ?>) o;
        return Objects.equals(function, that.function) &&
                Objects.equals(operator, that.operator) &&
                Objects.equals(queryValueTerm, that.queryValueTerm);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(function, operator, queryValueTerm);
    }
    
    @Override
    public boolean test(T entity) {
        R value = function.apply(entity);
        if (operator == CONTAINS) {
            return (Boolean) value;
        }
        if (operator != null) {
            int cmp = value.compareTo(queryValueTerm);
            switch (operator) {
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
        } else {
            String valueToString = value.toString();
            return valueToString.startsWith(queryPrefixTerm);
        }
    }
}
