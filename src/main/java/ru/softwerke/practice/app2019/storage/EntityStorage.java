package ru.softwerke.practice.app2019.storage;

import ru.softwerke.practice.app2019.model.Entity;
import ru.softwerke.practice.app2019.query.Query;
import ru.softwerke.practice.app2019.query.QueryCondition;
import ru.softwerke.practice.app2019.query.QueryConditionsHolder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityStorage<T extends Entity> implements Storage<T> {
    
    private Map<Long, T> entityMap = new ConcurrentHashMap<>();
    
    @Override
    public T addEntity(T entity) {
        entityMap.put(entity.getId(), entity);
        return entity;
    }
    
    @Override
    public List<T> getFilterOrderListFromStorage(Query<T> query) {
        QueryConditionsHolder<T> queryTerms = query.getConditionsHolder();
        Collection<QueryCondition<T, ?>> conditions = queryTerms.getQueryConditions();
        Stream<T> entityStream = entityMap.values().stream();
        for (QueryCondition<T, ?> condition : conditions) {
            entityStream = entityStream.filter(condition);
        }
        Comparator<T> sortingComparator = queryTerms.getQueryComparator();
        if (sortingComparator != null) {
            entityStream = entityStream.sorted(sortingComparator);
        }
        if (!queryTerms.isPaginatedOutput()) {
            return entityStream
                    .skip(queryTerms.getOffset())
                    .limit(queryTerms.getCount())
                    .collect(Collectors.toList());
        } else {
            return entityStream
                    .skip(queryTerms.getCount() * (queryTerms.getPage() - 1))
                    .limit(queryTerms.getCount())
                    .collect(Collectors.toList());
        }
    }
    
    @Override
    public T getEntityById(long id) {
        return entityMap.get(id);
    }
}