package com.example.authservice.exception.token;

public class TokenNotFoundException extends RuntimeException{
    private String message;
    public TokenNotFoundException(String message){
        super(message);
    }
}
