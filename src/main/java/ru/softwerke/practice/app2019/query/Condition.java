package ru.softwerke.practice.app2019.query;

public interface Condition<T> {
    boolean test(T entity);
}
