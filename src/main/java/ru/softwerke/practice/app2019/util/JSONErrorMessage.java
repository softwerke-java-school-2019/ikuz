package ru.softwerke.practice.app2019.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.ws.rs.core.Response;

/**
 * JSON error representation
 */
public class JSONErrorMessage {
    private final ErrorDescription error;
    
    private JSONErrorMessage(ErrorDescription e) {
        this.error = e;
    }
    
    /**
     * Constructs error representation
     *
     * @param type   error type
     * @param msg    error message
     * @param status error status
     * @return error representation
     */
    public static JSONErrorMessage create(Response.Status status, String type, String msg) {
        return new JSONErrorMessage(new ErrorDescription(status, type, msg));
    }
    
    public ErrorDescription getError() {
        return error;
    }
    
    public static class ErrorDescription {
        private final String type;
        private final String message;
        private final Response.Status status;
    
        private ErrorDescription(Response.Status status, String type, String msg) {
            this.type = type;
            this.message = msg;
            this.status = status;
        }
    
        public String getType() {
            return type;
        }
    
        public String getMessage() {
            return message;
        }
        @JsonIgnore
        public Response.Status getStatus() {
            return status;
        }
    }
}
