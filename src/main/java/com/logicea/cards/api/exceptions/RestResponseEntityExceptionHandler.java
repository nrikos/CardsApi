package com.logicea.cards.api.exceptions;

import com.logicea.cards.api.payloads.CardsApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CardsApiException.class)
    protected ResponseEntity<Object> customHandleConflictFound(CardsApiException ex) {
        String bodyOfResponse = ex.getMessage();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(bodyOfResponse, httpHeaders,
                ex.getStatus() != null ? ex.getStatus() : HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder sb = new StringBuilder();
        ex.getAllErrors().forEach(objectError ->
                sb.append(objectError.getDefaultMessage())
                        .append(ex.getAllErrors().indexOf(objectError) == ex.getAllErrors().size() - 1 ? "" : ", ")
        );
        String bodyOfResponse = CardsApiError.VALIDATION_ERROR.asJsonString(sb.toString());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(bodyOfResponse, headers, status);
    }


}
