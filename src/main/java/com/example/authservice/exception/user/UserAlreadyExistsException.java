package com.example.authservice.exception.user;

public class UserAlreadyExistsException extends RuntimeException{
    private String message;
    public UserAlreadyExistsException(String message){
        super(message);
    }
}
