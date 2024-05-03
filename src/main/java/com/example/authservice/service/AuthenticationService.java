package com.example.authservice.service;

import com.example.authservice.dto.AuthenticationResponse;
import com.example.authservice.dto.LoginRequest;
import com.example.authservice.dto.UserCreateRequest;

public interface AuthenticationService {
    AuthenticationResponse register(UserCreateRequest request);

    AuthenticationResponse authenticate(LoginRequest request);

    String logout();
}
