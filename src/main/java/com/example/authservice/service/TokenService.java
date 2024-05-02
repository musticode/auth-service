package com.example.authservice.service;

import com.example.authservice.model.Token;
import com.example.authservice.model.User;

public interface TokenService {
    Token findToken(String token);
    Token findTokenWithId(long id);

    void saveToken(Token token);
    void saveUserToken(String jwt, User user);

    void revokeAllTokenByUser(User user);
}
