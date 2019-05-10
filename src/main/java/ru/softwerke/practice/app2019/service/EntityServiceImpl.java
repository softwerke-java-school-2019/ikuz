package ru.softwerke.practice.app2019.service;

import ru.softwerke.practice.app2019.model.Entity;
import ru.softwerke.practice.app2019.query.Query;
import ru.softwerke.practice.app2019.storage.Storage;
import ru.softwerke.practice.app2019.util.ParseFromStringParam;
import ru.softwerke.practice.app2019.util.QueryUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class EntityServiceImpl<T extends Entity> implements EntityService<T> {
    private final Storage<T> storage;
    
    public EntityServiceImpl(Storage<T> storage) {
        this.storage = storage;
    }
    
    @Override
    public T putEntity(T newEntity) throws WebApplicationException {
        if (newEntity == null) {
            Response response = QueryUtils.getResponseWithMessage(
                    Response.Status.BAD_REQUEST,
                    QueryUtils.EMPTY_REQUEST_TYPE_ERROR,
                    "request body is empty"
            );
            throw new WebApplicationException(response);
        }
        return storage.addEntity(newEntity);
    }
    
    @Override
    public T getEntity(String entityTypeName, String idByString) throws WebApplicationException {
        ParseFromStringParam<Long> idParam = new ParseFromStringParam<>(
                idByString,
                Query.ID,
                ParseFromStringParam.PARSE_LONG_FUN,
                ParseFromStringParam.POSITIVE_NUMBER_FORMAT
        );
        long id = idParam.getParsedValue();
        checkDoesContainEntityWithId(entityTypeName, id);
        return storage.getEntityById(id);
    }
    
    @Override
    public List<T> getRequestedListOfEntities(Query<T> query) throws WebApplicationException {
        if (storage.getFilterOrderListFromStorage(query).isEmpty()) {
            Response response = QueryUtils.getResponseWithMessage(
                    Response.Status.NOT_FOUND,
                    QueryUtils.NOT_FOUND_TYPE_ERROR,
                    "Nothing found for this request"
            );
            throw new WebApplicationException(response);
        }
        return storage.getFilterOrderListFromStorage(query);
    }
    
    @Override
    public void checkDoesContainEntityWithId(String entityTypeName, long id) throws WebApplicationException {
        T result = storage.getEntityById(id);
        if (result == null) {
            Response response = QueryUtils.getResponseWithMessage(
                    Response.Status.NOT_FOUND,
                    QueryUtils.NOT_FOUND_TYPE_ERROR,
                    String.format("%s with id %d not found", entityTypeName, id)
            );
            throw new WebApplicationException(response);
        }
    }
}
