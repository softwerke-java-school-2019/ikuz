package ru.softwerke.practice.app2019.controller.rest;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import ru.softwerke.practice.app2019.model.Entity;
import ru.softwerke.practice.app2019.query.Query;
import ru.softwerke.practice.app2019.query.QueryConditionsHolder;
import ru.softwerke.practice.app2019.util.LongParam;
import ru.softwerke.practice.app2019.util.QueryUtils;
import ru.softwerke.practice.app2019.service.EntityService;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class BaseRestController<T extends Entity> implements RestController<T> {
    EntityService<T> service;
    
    Query.Builder<T> queryBuilder;
    Comparator<T> entityComparator;
    
    public T getEntityById(String idByString, String entity) throws WebApplicationException {
        LongParam idParam = new LongParam(idByString, Query.ID, Query.GET_BY_ID);
        long id = idParam.getLongValue();
        T result = service.getEntity(id);
        if (result == null) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.NOT_FOUND,
                            "entity doesn't exist",
                            String.format("%s with id %d doesn't exist", entity, id));
            throw new WebApplicationException(response);
        }
        return result;
    }
    
    public List<T> getEntities(Query<T> query) throws WebApplicationException {
        QueryConditionsHolder<T> allConditions = query.getConditionsHolder();
        List<T> result = service.getRequestedListOfEntities(allConditions);
        if (result.isEmpty()) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.NOT_FOUND,
                            "not found",
                            "nothing found for this request");
            throw new WebApplicationException(response);
        }
        return result;
    }
    
    public T addNewEntity(T newEntity) throws WebApplicationException {
        if (newEntity == null) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            "empty request",
                            "request body is empty");
            throw new WebApplicationException(response);
        }
        return service.putEntity(newEntity);
    }
    
    MultivaluedMap<String, String> getCommonQueryProperty(UriInfo uriInfo) throws WebApplicationException {
        QueryUtils.checkLengthQuery(uriInfo.getPath().length());
        MultivaluedMap<String, String> queryParams = new MultivaluedStringMap(uriInfo.getQueryParameters());
        for (String key : queryParams.keySet()) {
            switch (key) {
                case Query.COUNT: {
                    queryBuilder.setCount(new LongParam(queryParams.getFirst(key), Query.COUNT, Query.FILTER));
                    break;
                }
                case Query.PAGE: {
                    queryBuilder.setPage(new LongParam(queryParams.getFirst(key), Query.PAGE, Query.FILTER));
                    break;
                }
                case Query.OFFSET: {
                    queryBuilder.setOffset(new LongParam(queryParams.getFirst(key), Query.OFFSET, Query.FILTER));
                    break;
                }
            }
        }
        if (!queryParams.containsKey(Query.PAGE) && queryParams.containsKey(Query.OFFSET)) {
            queryBuilder.changeDisplayToOffsetOption();
        }
        queryParams.remove(Query.COUNT);
        queryParams.remove(Query.PAGE);
        queryParams.remove(Query.OFFSET);
        return queryParams;
    }
}
