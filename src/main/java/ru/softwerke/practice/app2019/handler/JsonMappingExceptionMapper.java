package ru.softwerke.practice.app2019.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
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
        if (exception.getCause() instanceof WebApplicationException) {
            return ((WebApplicationException) exception.getCause()).getResponse();
        } else {
            JSONErrorMessage message = JSONErrorMessage.create(
                    Response.Status.BAD_REQUEST,
                    QueryUtils.INVALID_JSON_ERROR,
                    exception.getOriginalMessage()
            );
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(message)
                    .build();
        }
    }
}
