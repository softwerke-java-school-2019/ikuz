package ru.softwerke.practice.app2019.storage;

import ru.softwerke.practice.app2019.model.Entity;
import ru.softwerke.practice.app2019.query.QueryCondition;
import ru.softwerke.practice.app2019.query.QueryConditionsHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityStorage<T extends Entity> implements Storage<T> {
    
    private List<T> entityList = Collections.synchronizedList(new ArrayList<>());
    
    @Override
    public T addEntity(T entity) {
        entityList.add(entity);
        return entity;
    }
    
    @Override
    public List<T> getRequestedListOfEntitiesFromStorage(QueryConditionsHolder<T> queryTerms) {
        List<QueryCondition<T, ?>> conditions = queryTerms.getQueryConditionList();
        Stream<T> entityStream = entityList.stream();
        for (QueryCondition<T, ?> condition : conditions) {
            entityStream = entityStream.filter(condition::test);
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
        return entityList
                .stream()
                .filter(entity -> entity.getId() == id)
                .findFirst()
                .orElse(null);
    }
}