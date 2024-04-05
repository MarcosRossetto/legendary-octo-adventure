package org.acme.exceptions.mappers;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ResourceBundle;
import java.util.UUID;

import org.acme.utils.ErrorResponseUtil;
import org.hibernate.exception.ConstraintViolationException;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {

    @Inject
    @Named("messages")
    ResourceBundle messages;

    @Override
    public Response toResponse(PersistenceException exception) {
        Throwable cause = exception.getCause();

        if (cause instanceof ConstraintViolationException) {
            return handleConstraintViolation((ConstraintViolationException) cause);
        } else if (cause instanceof SQLException) {
            return handleSQLException((SQLException) cause);
        } else {
            return handleGenericPersistenceException(exception);
        }
    }

    private Response handleConstraintViolation(ConstraintViolationException e) {
        return buildErrorResponse(MessageFormat.format(messages.getString("ERROR.CONSTRAINT_VIOLATION"), e.getMessage()), Response.Status.BAD_REQUEST);
    }

    private Response handleSQLException(SQLException e) {
        return buildErrorResponse(MessageFormat.format(messages.getString("ERROR.DATABASE_ERROR"), e.getMessage()), Response.Status.BAD_REQUEST);
    }

    private Response handleGenericPersistenceException(PersistenceException e) {
        return buildErrorResponse(MessageFormat.format(messages.getString("ERROR.PERSISTENCE_ERROR"), e.getMessage()), Response.Status.INTERNAL_SERVER_ERROR);
    }

    private Response buildErrorResponse(String message, Response.Status status) {
        String uuid = UUID.randomUUID().toString();
        String timestamp = Instant.now().toString();
        
        log.error(MessageFormat.format(messages.getString("LOG.ERROR.DETAILS"), uuid, status, message, timestamp));

        ErrorResponseUtil errorResponse = new ErrorResponseUtil(uuid, message, timestamp, status.getStatusCode());

        return Response.status(status).entity(errorResponse).type(MediaType.APPLICATION_JSON).build();
    }


}

