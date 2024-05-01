package com.fullcycle.subscription.infrastructure.configuration;

import com.fullcycle.subscription.domain.exceptions.DomainException;
import com.fullcycle.subscription.domain.validation.Error;
import com.fullcycle.subscription.domain.validation.handler.Notification;
import com.fullcycle.subscription.infrastructure.exceptions.ForbiddenException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.unprocessableEntity()
                .body(Notification.create(covertError(ex.getBindingResult().getAllErrors())));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<?> handleDomainException(DomainException ex) {
        return ResponseEntity.unprocessableEntity().body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbiddenException(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Error("Authentication", ex.getMessage()));
    }

    private List<Error> covertError(List<ObjectError> allErrors) {
        return allErrors.stream()
                .map(e -> new Error(((FieldError) e).getField(), e.getDefaultMessage()))
                .toList();
    }
}
