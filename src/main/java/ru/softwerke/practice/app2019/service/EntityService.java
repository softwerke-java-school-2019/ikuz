package ru.softwerke.practice.app2019.service;

import ru.softwerke.practice.app2019.model.Entity;
import ru.softwerke.practice.app2019.query.QueryConditionsHolder;

import javax.ws.rs.NotFoundException;
import java.util.List;

public interface EntityService<T extends Entity> {
    T putEntity(T entity);

    T getEntity(long id) throws NotFoundException;

    List<T> getRequestedListOfEntities(QueryConditionsHolder<T> queryTerms);
}
