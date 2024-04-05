package org.acme.exceptions.mappers;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.ResourceBundle;
import java.util.UUID;

import org.acme.exceptions.NotFoundException;
import org.acme.utils.ErrorResponseUtil;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Inject
    @Named("messages")
    ResourceBundle messages;

    @Override
    public Response toResponse(NotFoundException exception) {
        String uuid = UUID.randomUUID().toString();
        String timestamp = Instant.now().toString();
        String message = exception.getMessage();
        Response.Status status = Response.Status.NOT_FOUND;
        
        log.error(MessageFormat.format(messages.getString("LOG.ERROR.DETAILS"), uuid, status, message, timestamp));
        ErrorResponseUtil errorResponse = new ErrorResponseUtil(uuid, message, timestamp, status.getStatusCode());
        
        return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
    }
}