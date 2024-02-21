package com.fullcycle.codeflix.subscription.infrastructure.exceptions;

import com.fullcycle.codeflix.subscription.domain.validation.handler.Notification;
import com.fullcycle.codeflix.subscription.infrastructure.utils.ErrorUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.unprocessableEntity()
                .body(Notification.create(ErrorUtils.convert(ex.getBindingResult().getAllErrors())));
    }
}
