package ru.softwerke.practice.app2019.controller.rest;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import ru.softwerke.practice.app2019.model.Entity;
import ru.softwerke.practice.app2019.query.Query;
import ru.softwerke.practice.app2019.query.QueryConditionsHolder;
import ru.softwerke.practice.app2019.util.IntegerParam;
import ru.softwerke.practice.app2019.util.QueryUtils;
import ru.softwerke.practice.app2019.service.EntityService;

import javax.ws.rs.WebApplicationException;
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
        IntegerParam idParam = new IntegerParam(idByString, Query.ID, Query.GET_BY_ID);
        int id = idParam.getIntegerValue();
        T result = service.getEntity(id);
        if (result == null) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.NOT_FOUND,
                            QueryUtils.NOT_FOUND_TYPE_ERROR,
                            String.format("%s with id %d not found", entity, id));
            throw new WebApplicationException(response);
        }
        return result;
    }
    
    public List<T> getEntities(Query<T> query) throws WebApplicationException {
        QueryConditionsHolder<T> allConditions = query.getConditionsHolder();
        return service.getRequestedListOfEntities(allConditions);
    }
    
    public T addNewEntity(T newEntity) throws WebApplicationException {
        if (newEntity == null) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            QueryUtils.EMPTY_REQUEST_TYPE_ERROR,
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
                    queryBuilder.setCount(new IntegerParam(queryParams.getFirst(key), Query.COUNT, Query.FILTER));
                    break;
                }
                case Query.PAGE: {
                    queryBuilder.setPage(new IntegerParam(queryParams.getFirst(key), Query.PAGE, Query.FILTER));
                    break;
                }
                case Query.OFFSET: {
                    queryBuilder.setOffset(new IntegerParam(queryParams.getFirst(key), Query.OFFSET, Query.FILTER));
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
    
    void addOrderType(List<String> allOrderTypes, Map<String, Comparator<T>> orderParamsMap, String name) throws WebApplicationException {
        for (String type : allOrderTypes) {
            if (orderParamsMap.containsKey(type)) {
                if (entityComparator == null) {
                    entityComparator = orderParamsMap.get(type);
                } else {
                    entityComparator = entityComparator.thenComparing(orderParamsMap.get(type));
                }
            } else {
                Response response = QueryUtils.
                        getResponseWithMessage(Response.Status.BAD_REQUEST,
                                QueryUtils.MALFORMED_PARAMS_TYPE_ERROR,
                                QueryUtils.getMalformedValueParamsMessage(type, name));
                throw new WebApplicationException(response);
            }
        }
    }
    
    void sendWrongParamsMessage(String key, String name) throws WebApplicationException {
        Response response = QueryUtils.
                getResponseWithMessage(Response.Status.BAD_REQUEST,
                        QueryUtils.MALFORMED_PARAMS_TYPE_ERROR,
                        QueryUtils.getMalformedKeyParamsMessage(name, key));
        throw new WebApplicationException(response);
    }
}
