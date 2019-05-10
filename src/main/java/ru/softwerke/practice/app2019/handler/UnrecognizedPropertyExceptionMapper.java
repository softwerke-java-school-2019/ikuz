package ru.softwerke.practice.app2019.handler;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import ru.softwerke.practice.app2019.util.QueryUtils;

import javax.annotation.Priority;
import javax.inject.Singleton;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


/**
 * Handler of JSON unrecognized field errors. Automatically responds with Bad Request on error
 */
@Provider
@Priority(1)
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class UnrecognizedPropertyExceptionMapper implements ExceptionMapper<UnrecognizedPropertyException> {
    
    @Override
    public Response toResponse(UnrecognizedPropertyException exception) {
        JSONErrorMessage message = JSONErrorMessage.create(
                Response.Status.BAD_REQUEST,
                QueryUtils.WRONG_PROPERTY_TYPE_ERROR,
                QueryUtils.getWrongPropertyMessage(exception.getPropertyName())
        );
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(message)
                .build();
    }
}
