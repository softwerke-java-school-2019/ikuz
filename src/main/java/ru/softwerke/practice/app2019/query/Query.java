package ru.softwerke.practice.app2019.query;

import ru.softwerke.practice.app2019.model.Device;
import ru.softwerke.practice.app2019.model.Entity;
import ru.softwerke.practice.app2019.util.IntegerParam;
import ru.softwerke.practice.app2019.util.QueryUtils;

import java.util.Comparator;

public class Query<T extends Entity> {
    public static final String COUNT = "pageItems";
    public static final String OFFSET = "offset";
    public static final String PAGE = "page";
    public static final String ORDER_TYPE = "orderBy";
    public static final String ID = "ID";
    public static final String POST_ENTITY = "post ";
    public static final String GET_BY_ID = "get by ID";
    public static final String FILTER = "filter ";
    
    QueryConditionsHolder<T> holder;
    
    Query() {
        this.holder = new QueryConditionsHolder<>();
    }
    
    public QueryConditionsHolder<T> getConditionsHolder() {
        return holder;
    }
    
    public static class Builder<T extends Entity> {
        Query<T> queryToBuild;
        
        Builder() {
            queryToBuild = new Query<>();
        }
        
        public Builder setCount(IntegerParam count) {
            int parsedCountValue = count.getIntegerValue();
            QueryUtils.checkCountValue(parsedCountValue);
            this.queryToBuild.holder.setCountOfElementsParam(parsedCountValue);
            return this;
        }
        
        public Builder setPage(IntegerParam page) {
            this.queryToBuild.holder.setPageParam(page.getIntegerValue());
            return this;
        }
        
        public Builder setOffset(IntegerParam offset) {
            this.queryToBuild.holder.setOffsetParam(offset.getIntegerValue());
            return this;
        }
        
        public Builder changeDisplayToOffsetOption() {
            this.queryToBuild.holder.setDisplayToOffsetParam();
            return this;
        }
        
        public Query.Builder setComparator(Comparator<T> comparator) {
            this.queryToBuild.holder.setQueryComparator(comparator);
            return this;
        }
        
        public Query build() {
            Query builtEntity = queryToBuild;
            queryToBuild = new Query<>();
            
            return builtEntity;
        }
    }
}
