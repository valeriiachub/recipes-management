package org.mendix.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mendix.dto.response.ErrorResponse;
import org.mendix.exception.EnumSerializeException;
import org.mendix.exception.RecipeValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.Date;

import javax.persistence.EntityNotFoundException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(RecipeValidationException.class)
    @RequestMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<ErrorResponse> handleRecipeBadRequestException(RecipeValidationException e) {
        log.warn("[EXCEPTION] ExceptionMessage:: {}", e.getMessage(), e);
        ErrorResponse errorResponse = createErrorResponse(BAD_REQUEST, e.getMessage());

        return new ResponseEntity<>(errorResponse, prepareHttpHeaders(), BAD_REQUEST);
    }

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(EnumSerializeException.class)
    @RequestMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<ErrorResponse> handleEnumSerializationException(EnumSerializeException e) {
        log.warn("[EXCEPTION] ExceptionMessage:: {}", e.getMessage(), e);
        ErrorResponse errorResponse = createErrorResponse(BAD_REQUEST, e.getMessage());

        return new ResponseEntity<>(errorResponse, prepareHttpHeaders(), BAD_REQUEST);
    }

    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    @RequestMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("[EXCEPTION] ExceptionMessage:: {}", e.getMessage(), e);
        ErrorResponse errorResponse = createErrorResponse(BAD_REQUEST, e.getMessage());

        return new ResponseEntity<>(errorResponse, prepareHttpHeaders(), BAD_REQUEST);
    }

    @ResponseStatus(UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    @RequestMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("[EXCEPTION] ExceptionMessage:: {}", e.getMessage(), e);
        ErrorResponse errorResponse = createErrorResponse(UNAUTHORIZED, e.getMessage());

        return new ResponseEntity<>(errorResponse, prepareHttpHeaders(), UNAUTHORIZED);
    }

    @ResponseStatus(UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    @RequestMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        log.warn("[EXCEPTION] ExceptionMessage:: {}", e.getMessage(), e);
        ErrorResponse errorResponse = createErrorResponse(UNAUTHORIZED, e.getMessage());
        return new ResponseEntity<>(errorResponse, prepareHttpHeaders(), UNAUTHORIZED);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @RequestMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<ErrorResponse> handleRuntimeException(Exception e) {
        log.warn("[EXCEPTION] ExceptionMessage:: {}", e.getMessage(), e);
        ErrorResponse errorResponse = createErrorResponse(INTERNAL_SERVER_ERROR, e.getMessage());
        return new ResponseEntity<>(errorResponse, prepareHttpHeaders(), INTERNAL_SERVER_ERROR);
    }

    private static ErrorResponse createErrorResponse(HttpStatus status, String e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(String.valueOf(status.value()));
        errorResponse.setErrors(Collections.singletonList(e));
        return errorResponse;
    }

    private HttpHeaders prepareHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);

        return headers;
    }
}
