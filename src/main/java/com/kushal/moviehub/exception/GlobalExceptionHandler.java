package com.kushal.moviehub.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kushal.moviehub.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMovieNotFound(
            MovieNotFoundException exception) {

        return new ResponseEntity<>(
                new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        exception.getMessage()
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException exception) {

        return new ResponseEntity<>(
                new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        exception.getMessage()
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReviewNotFound(
            ReviewNotFoundException exception) {

        return new ResponseEntity<>(
                new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        exception.getMessage()
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(
            UserAlreadyExistsException exception) {

        return new ResponseEntity<>(
                new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        exception.getMessage()
                ),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(WatchlistAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse>
            handleWatchlistAlreadyExists(
                    WatchlistAlreadyExistsException exception) {

        return new ResponseEntity<>(
                new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        exception.getMessage()
                ),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(WatchlistNotFoundException.class)
    public ResponseEntity<ErrorResponse>
            handleWatchlistNotFound(
                    WatchlistNotFoundException exception) {

        return new ResponseEntity<>(
                new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        exception.getMessage()
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException exception) {

        return new ResponseEntity<>(
                new ErrorResponse(
                        HttpStatus.FORBIDDEN.value(),
                        exception.getMessage()
                ),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return new ResponseEntity<>(
                new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Validation failed",
                        errors
                ),
                HttpStatus.BAD_REQUEST
        );
    }
}