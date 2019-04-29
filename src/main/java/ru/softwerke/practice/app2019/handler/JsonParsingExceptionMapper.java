package ru.softwerke.practice.app2019.handler;

import com.fasterxml.jackson.core.JsonParseException;
import ru.softwerke.practice.app2019.util.QueryUtils;

import javax.annotation.Priority;
import javax.inject.Singleton;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Handler of JSON miscellaneous parse errors. Automatically responds with Bad Request on error
 */
@Provider
@Priority(2)
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class JsonParsingExceptionMapper implements ExceptionMapper<JsonParseException> {
    
    @Override
    public Response toResponse(JsonParseException exception) {
        JSONErrorMessage message = JSONErrorMessage.create(Response.Status.BAD_REQUEST,
                QueryUtils.PARSING_TYPE_ERROR, exception.getOriginalMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
    }
}
