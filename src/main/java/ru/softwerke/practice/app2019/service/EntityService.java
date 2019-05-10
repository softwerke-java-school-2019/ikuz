package ru.softwerke.practice.app2019.service;

import ru.softwerke.practice.app2019.model.Entity;
import ru.softwerke.practice.app2019.query.Query;

import javax.ws.rs.WebApplicationException;
import java.util.List;

/**
 * Entity-independent service
 *
 * @param <T> entity type
 */
public interface EntityService<T extends Entity> {
    /**
     * Add entity to storage
     *
     * @param entity entity
     * @return entity that has been added to storage
     * @throws WebApplicationException with response's status 400 {@code Response.Status.BAD_REQUEST}
     *         if the entity deserialization was incorrect and the efficiency is null
     */
    T putEntity(T entity) throws WebApplicationException;
    
    /**
     * Get entity from storage by identifier
     *
     * @param entityTypeName the string representing the name of the entity
     * @param idByString the string representing the entity identifier
     * @return entity by identifier from storage
     * @throws WebApplicationException
     *         if the identifier in the string is incorrect or an entity with such an identifier does not exist
     */
    T getEntity(String entityTypeName, String idByString) throws WebApplicationException;
    
    /**
     * Get list of entities from the storage according the filter, order query
     *
     * @param query the filter order entity's query
     * @return list of entities matching filtering and sorting query
     * @throws WebApplicationException with response's status 404 {@code Response.Status.NOT_FOUND}
     *         if nothing was found for the query
     */
    List<T> getRequestedListOfEntities(Query<T> query) throws WebApplicationException;
    
    /**
     * Checks if the repository contains the entity with the given identifier
     *
     * @param entityTypeName the string representing the name of the entity
     * @param id the entity identifier
     * @throws WebApplicationException with response's status 404 {@code Response.Status.NOT_FOUND}
     *         if an entity with such an identifier does not exist
     */
    void checkDoesContainEntityWithId(String entityTypeName, long id) throws WebApplicationException;
}
