package com.dev.mtrs.projects.qualifyguruv2.infrastructure.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // 1. Handling Custom Domain Exceptions (e.g., Max Resumes Exceeded)
    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleDomainIllegalState(IllegalStateException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());

        problemDetail.setType(URI.create("https://api.qualifyguru.com/errors/conflict"));
        problemDetail.setTitle("Business Rule Violation");

        return problemDetail;
    }

    // 2. Handling Validation Errors (@Valid in Controllers)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Payload validation failed");

        problemDetail.setType(URI.create("https://api.qualifyguru.com/errors/validation-failed"));
        problemDetail.setTitle("Validation Failed");

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        problemDetail.setProperty("invalid_params", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    // 3. Handling Unexpected Server Errors (Catch-all)
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAllUncaughtException(Exception ex, WebRequest request) {

        logger.error("An unexpected internal server error occurred", ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later."
        );
        problemDetail.setType(URI.create("https://api.qualifyguru.com/errors/internal-server-error"));
        problemDetail.setTitle("Internal Server Error");
        return problemDetail;
    }
}
