package com.example.authservice.exception.user;

public class UserNotFoundException extends RuntimeException{
    private String message;

    public UserNotFoundException(String message){
        super(message);
    }
}
