package ru.softwerke.practice.app2019.storage;

import ru.softwerke.practice.app2019.model.Entity;
import ru.softwerke.practice.app2019.query.QueryConditionsHolder;

import javax.ws.rs.NotFoundException;
import java.util.List;

public interface Storage<T extends Entity> {
    T addEntity(T entity);
    List<T> getRequestedListOfEntitiesFromStorage(QueryConditionsHolder<T> queryTerms);
    T getEntityById(long id) throws NotFoundException;
}
