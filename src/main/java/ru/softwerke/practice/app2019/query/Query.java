package ru.softwerke.practice.app2019.query;

import ru.softwerke.practice.app2019.model.Entity;
import ru.softwerke.practice.app2019.util.LongParam;
import ru.softwerke.practice.app2019.util.QueryUtils;

import java.util.Comparator;

public class Query<T extends Entity> {
    public static final String COUNT = "count";
    public static final String OFFSET = "offset";
    public static final String PAGE = "page";
    public static final String ORDER_TYPE = "orderBy";
    public static final String ID = "ID";
    public static final String ADD_ENTITY = "add";
    public static final String GET_BY_ID = "get by ID";
    public static final String FILTER = "filter";
    public static final String ORDER = "order";
    QueryConditionsHolder<T> holder;
    
    Query() {
        this.holder = new QueryConditionsHolder<>();
    }
    
    public QueryConditionsHolder<T> getConditionsHolder() {
        return holder;
    }
    
    public static class Builder<T extends Entity> {
        private Query<T> queryToBuild;
        
        
        public Builder() {
            queryToBuild = new Query<>();
        }
        
        public Query.Builder setCount(LongParam count) {
            long parsedCountValue = count.getLongValue();
            parsedCountValue = QueryUtils.checkCountValue(parsedCountValue);
            this.queryToBuild.holder.setCountOfElementsParam(parsedCountValue);
            return this;
        }
        
        public Query.Builder setPage(LongParam page) {
            this.queryToBuild.holder.setPageParam(page.getLongValue());
            return this;
        }
        
        public Query.Builder setOffset(LongParam offset) {
            this.queryToBuild.holder.setOffsetParam(offset.getLongValue());
            return this;
        }
        
        public Query.Builder changeDisplayToOffsetOption() {
            this.queryToBuild.holder.setDisplayToOffsetParam();
            return this;
        }
        
        public Query build() {
            Query builtEntity = queryToBuild;
            queryToBuild = new Query<>();
            
            return builtEntity;
        }
    }
}
