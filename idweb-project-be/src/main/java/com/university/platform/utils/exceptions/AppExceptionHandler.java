package com.university.platform.utils.exceptions;

import com.university.platform.api.exchange.Error;
import com.university.platform.api.exchange.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionResponse(BAD_REQUEST, ex);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleUnknownExceptions(Exception ex) {
        return handleExceptionResponse(INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler(PlatformException.class)
    protected ResponseEntity<?> handleApplicationExceptions(Exception ex) {
        return handleExceptionResponse(HttpStatus.valueOf(((PlatformException) ex).getExceptionType().getStatusCode()),
                ex);
    }

    private ResponseEntity<Object> handleExceptionResponse(HttpStatus status, Throwable exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(status).body(Response.build(
                Error.build(status, exception.getMessage(), LocalDateTime.now())));
    }

}
