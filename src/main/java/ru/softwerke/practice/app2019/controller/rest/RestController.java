package ru.softwerke.practice.app2019.controller.rest;

import ru.softwerke.practice.app2019.model.Entity;
import ru.softwerke.practice.app2019.query.Query;

import javax.ws.rs.WebApplicationException;
import java.util.List;

public interface RestController<T extends Entity> {
    T getEntityById(String idByString, String entity) throws WebApplicationException;
    
    List<T> getEntities(Query<T> query) throws WebApplicationException;
    
    T addNewEntity(T newEntity) throws WebApplicationException;
}
