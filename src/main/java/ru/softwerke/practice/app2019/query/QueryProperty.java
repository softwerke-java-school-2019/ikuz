package ru.softwerke.practice.app2019.query;

import ru.softwerke.practice.app2019.model.Entity;

import java.util.Objects;
import java.util.function.Function;

public class QueryProperty<T extends Entity, R extends Comparable<? super R>> {
    private final Function<T, R> function;
    private final FilterOperator operator;
    
    QueryProperty(Function<T, R> function, FilterOperator operator) {
        this.function = function;
        this.operator = operator;
    }
    
    Function<T, R> getFunction() {
        return function;
    }
    
    FilterOperator getOperator() {
        return operator;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryProperty<?, ?> that = (QueryProperty<?, ?>) o;
        return Objects.equals(function, that.function) &&
                operator == that.operator;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(function, operator);
    }
}
