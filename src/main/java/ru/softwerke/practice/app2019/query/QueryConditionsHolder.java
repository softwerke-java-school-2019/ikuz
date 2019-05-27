package ru.softwerke.practice.app2019.query;

import ru.softwerke.practice.app2019.model.Entity;

import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The class stores all the conditions of the query: filtering, sorting comparator, the number of pages,
 * the number of objects on the page or an offset, if the page parameter is not specified
 *
 * @param <T> considered entity type
 */
public class QueryConditionsHolder<T extends Entity> {
    private static final long DEFAULT_COUNT = 10;
    private static final long DEFAULT_PAGE = 1;
    private static final long DEFAULT_OFFSET = 0;
    
    private final Collection<QueryCondition<T, ?>> queryConditions;
    private Comparator<T> queryComparator;
    private long count;
    private long page;
    private long offset;
    private boolean isPaginatedOutput;
    
    public QueryConditionsHolder() {
        this.queryConditions = new ConcurrentLinkedQueue<>();
        this.count = DEFAULT_COUNT;
        this.page = DEFAULT_PAGE;
        this.offset = DEFAULT_OFFSET;
        this.isPaginatedOutput = Boolean.TRUE;
    }
    
    public void addFilterCondition(QueryCondition<T, ?> queryCondition) {
        if (!queryConditions.contains(queryCondition)) {
            queryConditions.add(queryCondition);
        }
    }
    
    public void setCountOfElementsParam(long count) {
        this.count = count;
    }
    
    public void setPageParam(long page) {
        this.page = page;
    }
    
    public void setOffsetParam(long offset) {
        this.offset = offset;
    }
    
    public void changeDisplayToOffsetOption() {
        this.isPaginatedOutput = false;
    }
    
    public void setQueryComparator(Comparator<T> queryComparator) {
        this.queryComparator = queryComparator;
    }
    
    public long getCount() {
        return count;
    }
    
    public long getPage() {
        return page;
    }
    
    public long getOffset() {
        return offset;
    }
    
    public boolean isPaginatedOutput() {
        return isPaginatedOutput;
    }
    
    public Collection<QueryCondition<T, ?>> getQueryConditions() {
        return queryConditions;
    }
    
    public Comparator<T> getQueryComparator() {
        return queryComparator;
    }
}
