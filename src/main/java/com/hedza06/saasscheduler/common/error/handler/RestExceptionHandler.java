package com.hedza06.saasscheduler.common.error.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hedza06.saasscheduler.auth.application.exceptions.AdminTokenNotFoundException;
import com.hedza06.saasscheduler.common.error.exception.EntityNotFoundException;
import com.hedza06.saasscheduler.common.error.exception.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ValidationException.class)
  protected ResponseEntity<Object> handleValidationError(ValidationException ex) {
    var errors = new ArrayList<String>();
    for (FieldError error : ex.getErrors().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    var errorResponse = new ApiErrorResponse("Validation failed", errors);
    return respondWith(HttpStatus.BAD_REQUEST, errorResponse);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  protected ResponseEntity<Object> handleEntityNotFoundError(EntityNotFoundException ex) {
    var errorResponse = new ApiErrorResponse(ex.getMessage(), emptyList());
    return respondWith(HttpStatus.BAD_REQUEST, errorResponse);
  }

  @ExceptionHandler(AdminTokenNotFoundException.class)
  protected ResponseEntity<Object> handleAdminTokenNotFoundError(AdminTokenNotFoundException ex) {
    var errorResponse = new ApiErrorResponse(ex.getMessage(), emptyList());
    return respondWith(HttpStatus.BAD_REQUEST, errorResponse);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatusCode status,
                                                                WebRequest request) {
    var errors = new ArrayList<String>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }
    var errorResponse = new ApiErrorResponse(ex.getMessage(), errors);
    return respondWith(HttpStatus.valueOf(status.value()), errorResponse);
  }

  private ResponseEntity<Object> respondWith(HttpStatus httpStatus,
                                             ApiErrorResponse apiErrorResponse) {
    return new ResponseEntity<>(apiErrorResponse, httpStatus);
  }

  record ApiErrorResponse(
      @JsonProperty(value = "message") String message,
      @JsonProperty(value = "errors") List<String> errors
  ) {
  }
}
