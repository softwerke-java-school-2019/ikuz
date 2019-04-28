package ru.softwerke.practice.app2019.query;

import ru.softwerke.practice.app2019.model.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QueryConditionsHolder<T extends Entity> {
    private static final int DEFAULT_COUNT = 10;
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_OFFSET = 0;
    
    private final List<QueryCondition<T, ?>> queryConditionList;
    private Comparator<T> queryComparator;
    private int count;
    private int page;
    private int offset;
    private boolean isPaginatedOutput;
    
    QueryConditionsHolder() {
        this.queryConditionList = Collections.synchronizedList(new ArrayList<>());
        this.count = DEFAULT_COUNT;
        this.page = DEFAULT_PAGE;
        this.offset = DEFAULT_OFFSET;
        this.isPaginatedOutput = Boolean.TRUE;
    }
    
    void addFilterCondition(QueryCondition<T, ?> queryCondition) {
        this.queryConditionList.add(queryCondition);
    }
    
    void setCountOfElementsParam(int count) {
        this.count = count;
    }
    
    void setPageParam(int page) {
        this.page = page;
    }
    
    void setOffsetParam(int offset) {
        this.offset = offset;
    }
    
    void setDisplayToOffsetParam() {
        this.isPaginatedOutput = false;
    }
    
    void setQueryComparator(Comparator<T> queryComparator) {
        this.queryComparator = queryComparator;
    }
    
    public int getCount() {
        return count;
    }
    
    public int getPage() {
        return page;
    }
    
    public int getOffset() {
        return offset;
    }
    
    public boolean isPaginatedOutput() {
        return isPaginatedOutput;
    }
    
    public List<QueryCondition<T, ?>> getQueryConditionList() {
        return queryConditionList;
    }
    
    public Comparator<T> getQueryComparator() {
        return queryComparator;
    }
}
