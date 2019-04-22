package ru.softwerke.practice.app2019.controller.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import ru.softwerke.practice.app2019.util.JSONErrorMessage;
import ru.softwerke.practice.app2019.util.QueryUtils;

import javax.annotation.Priority;
import javax.inject.Singleton;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Handler of JSON serialization/deserialization errors. Automatically responds with Bad Request on error
 */
@Provider
@Priority(1)
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class JsonMappingExceptionMapper implements ExceptionMapper<JsonMappingException> {
    
    @Override
    public Response toResponse(JsonMappingException exception) {
//        exception.printStackTrace();
        Throwable cause = exception.getCause();
        if(cause instanceof WebApplicationException) {
            return ((WebApplicationException) exception.getCause()).getResponse();
        } else {
            return QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            "invalid json",
                            "invalid json");
        }
//        JSONErrorMessage message = JSONErrorMessage.create("unrecognized property",
//                String.format("Unrecognized property '%s'", exception.getPropertyName()));
//        return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
    }
}
