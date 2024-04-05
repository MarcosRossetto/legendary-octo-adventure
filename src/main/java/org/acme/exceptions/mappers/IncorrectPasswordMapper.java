package org.acme.exceptions.mappers;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.ResourceBundle;
import java.util.UUID;

import org.acme.exceptions.IncorrectPasswordException;
import org.acme.utils.ErrorResponseUtil;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class IncorrectPasswordMapper implements ExceptionMapper<IncorrectPasswordException> {
    
    @Inject
    @Named("messages")
    ResourceBundle messages;

    @Override
    public Response toResponse(IncorrectPasswordException exception) {
        String uuid = UUID.randomUUID().toString();
        String timestamp = Instant.now().toString();
        String message = exception.getMessage();
        Response.Status status = Response.Status.UNAUTHORIZED;
        
        log.error(MessageFormat.format(messages.getString("LOG.ERROR.DETAILS"), uuid, status, message, timestamp));
        ErrorResponseUtil errorResponse = new ErrorResponseUtil(uuid, message, timestamp, status.getStatusCode());
        
        return Response.status(Response.Status.UNAUTHORIZED).entity(errorResponse).build();
    }
}
