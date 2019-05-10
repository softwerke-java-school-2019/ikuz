package ru.softwerke.practice.app2019.storage;

import ru.softwerke.practice.app2019.model.Entity;
import ru.softwerke.practice.app2019.query.Query;

import java.util.List;

public interface Storage<T extends Entity> {
    /**
     * Add an entity to corresponding entity's storage
     *
     * @param entity an entity to add
     * @return entity that has been added to storage
     */
    T addEntity(T entity);
    
    /**
     * Get list of entities from the storage according the filter, order query
     *
     * @param query the filter order entity's query
     * @return list of entities matching filtering and sorting query
     */
    List<T> getFilterOrderListFromStorage(Query<T> query);
    
    /**
     * Get entity from storage by identifier
     *
     * @param id the entity identifier
     * @return entity by identifier from storage
     */
    T getEntityById(long id);
}