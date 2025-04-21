package com.subhadeep.e_food_authentication_service.advices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.subhadeep.e_food_authentication_service.dto.ApiResponse;
import com.subhadeep.e_food_authentication_service.dto.ErrorResponse;
import com.subhadeep.e_food_authentication_service.exceptions.*;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(
            ResourceNotFoundException ex) {

        return buildResponseEntityFromErrorResponse(
                ErrorResponse.builder()
                        .status(404)
                        .error(ex.getMessage())
                        .subErrors(new ArrayList<>())
                        .build());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> handleBadRequestException(InvalidRequestException ex) {
        return buildResponseEntityFromErrorResponse(
                ErrorResponse.builder()
                        .status(400)
                        .error(ex.getMessage())
                        .subErrors(new ArrayList<>())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> hanleFieldValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return buildResponseEntityFromErrorResponse(
                ErrorResponse.builder()
                        .status(400)
                        .error("Validation fails")
                        .subErrors(errors.values().stream().toList())
                        .build());

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(cv -> {
            String field = cv.getPropertyPath().toString();
            Integer f = field.lastIndexOf(".");
            field = field.substring(++f);
            String message = cv.getMessage();
            errors.put(field, message);
        });

        return buildResponseEntityFromErrorResponse(

                ErrorResponse.builder()
                        .status(400)
                        .subErrors(errors.values().stream().toList())
                        .error(errors.keySet().stream().collect(Collectors.joining(",")) + " " +
                                (errors.size() == 1 ? "field" : "fields") + " "
                                + (errors.size() == 1 ? "is" : "are")
                                + " containing invalid " + (errors.size() == 1 ? "value" : "values")

                        )
                        .build());
    }

    @ExceptionHandler(InvalidCredException.class)
    public ResponseEntity<?> handleInvalidCredException(InvalidCredException ex) {

        return buildResponseEntityFromErrorResponse(
                ErrorResponse.builder()
                        .error(ex.getMessage())
                        .status(401)
                        .subErrors(new ArrayList<>())
                        .build());
    }

    private ResponseEntity<ApiResponse<ErrorResponse>> buildResponseEntityFromErrorResponse(
            ErrorResponse errorResponse) {

        ApiResponse<ErrorResponse> apiResponse = new ApiResponse<>(errorResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }
}
