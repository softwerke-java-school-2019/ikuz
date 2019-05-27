package ru.softwerke.practice.app2019.query;

import ru.softwerke.practice.app2019.model.Entity;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * Class representing filtering query condition
 *
 * @param <T> considered entity type
 * @param <R> object type corresponding to the filtering parameter considered entity
 */
public class QueryCondition<T extends Entity, R extends Comparable<? super R>> implements Predicate<T> {
    private Function<T, R> function;
    private FilterOperator operator;
    private R queryValueTerm;
    private String queryPrefixTerm;
    
    public QueryCondition(Function<T, R> function, FilterOperator operator, R queryValueTerm) {
        this.function = function;
        this.operator = operator;
        this.queryValueTerm = queryValueTerm;
    }
    
    public QueryCondition(Function<T, R> function, String queryPrefixTerm) {
        this.function = function;
        this.queryPrefixTerm = queryPrefixTerm;
    }
    
    public QueryCondition(Function<T, R> function, FilterOperator operator) {
        this.function = function;
        this.operator = operator;
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
    
    
    /**
     * Overridden method that checks an entity for compliance with a filtering query condition
     *
     * @param entity considered entity
     * @return {@code true} if considered entity satisfies the query otherwise {@code false}
     */
    @Override
    public boolean test(T entity) {
        R value = function.apply(entity);
        if (operator == FilterOperator.CONTAINS) {
            return (Boolean) value;
        }
        if (operator != null) {
            int cmp;
            if (value instanceof String && queryValueTerm instanceof String) {
                cmp = ((String) value).compareToIgnoreCase((String) queryValueTerm);
            } else  {
                cmp = value.compareTo(queryValueTerm);
            }
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
            String valueToString = value.toString().toLowerCase();
            return valueToString.startsWith(queryPrefixTerm.toLowerCase());
        }
    }
}
