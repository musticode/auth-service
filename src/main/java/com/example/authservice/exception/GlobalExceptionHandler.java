package com.example.authservice.exception;

import com.example.authservice.exception.token.TokenNotFoundException;
import com.example.authservice.exception.user.UserAlreadyExistsException;
import com.example.authservice.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException exception) {
        final ExceptionResponse response = ExceptionResponse.builder()
                .message("User not found")
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserAlreadyExistsException.class})
    public ResponseEntity<ExceptionResponse> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        final ExceptionResponse response = ExceptionResponse.builder()
                .message("User already exists")
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler({TokenNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleTokenNotFoundException(TokenNotFoundException exception) {
        final ExceptionResponse response = ExceptionResponse.builder()
                .message("Token not found")
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
