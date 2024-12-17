package com.nitor.ems.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleErrorResponse(EmployeeNotFoundException ex) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date().toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleErrorResponse(UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date().toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserInvalidException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUSerErrorResponse(UserInvalidException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date().toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmployeeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMismatchErrorResponse(EmployeeMismatchException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date().toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExist.class)
    public ResponseEntity<ErrorResponse> handleUserExistErrorResponse(UserAlreadyExist ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date().toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date().toString());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        errorResponse.setMessage("An Server Exception occurred!");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {

        Map<String, String> errors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });
        ErrorResponse errorResponse = createErrorResponse(
                errors.entrySet().stream().map(error -> new FieldError(error.getKey(), error.getKey(), error.getValue())).collect(Collectors.toList())
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse createErrorResponse(List<FieldError> fieldErrors) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date()));
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessage("Validation Failed");
        response.setFieldErrors(fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList()));
        return response;
    }
}
