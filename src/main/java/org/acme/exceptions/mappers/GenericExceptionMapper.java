package org.acme.exceptions.mappers;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

import org.acme.utils.ErrorResponseUtil;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Inject
    @Named("messages")
    ResourceBundle messages;

    @Override
    public Response toResponse(Throwable exception) {
        String uuid = UUID.randomUUID().toString();
        String timestamp = Instant.now().toString();
        String message = Optional.ofNullable(exception.toString()).orElse(messages.getString("ERROR.GENERIC"));
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

        String stackTrace = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n"));

        log.error(MessageFormat.format(messages.getString("LOG.ERROR.DETAILS"), uuid, status, stackTrace, timestamp));
        ErrorResponseUtil errorResponse = new ErrorResponseUtil(uuid, message, timestamp, status.getStatusCode());

        return Response.status(status).entity(errorResponse).build();
    }
}
