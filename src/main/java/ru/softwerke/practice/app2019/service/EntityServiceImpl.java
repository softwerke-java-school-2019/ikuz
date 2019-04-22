package ru.softwerke.practice.app2019.service;

import ru.softwerke.practice.app2019.model.Entity;
import ru.softwerke.practice.app2019.storage.Storage;
import ru.softwerke.practice.app2019.query.QueryConditionsHolder;

import java.util.List;

public class EntityServiceImpl<T extends Entity> implements EntityService<T> {
    private final Storage<T> storage;
    
    public EntityServiceImpl(Storage<T> storage) {
        this.storage = storage;
    }
    
    @Override
    public T putEntity(T entity) {
        return storage.addEntity(entity);
    }
    
    @Override
    public T getEntity(long id) {
        return storage.getEntityById(id);
    }
    
    @Override
    public List<T> getRequestedListOfEntities(QueryConditionsHolder<T> queryTerms) {
        return storage.getRequestedListOfEntitiesFromStorage(queryTerms);
    }
}
