package com.example.authservice.service.impl;

import com.example.authservice.dto.AuthenticationResponse;
import com.example.authservice.dto.LoginRequest;
import com.example.authservice.dto.UserCreateRequest;
import com.example.authservice.exception.user.UserAlreadyExistsException;
import com.example.authservice.exception.user.UserNotFoundException;
import com.example.authservice.model.Role;
import com.example.authservice.model.User;
import com.example.authservice.repository.TokenRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.AuthenticationService;
import com.example.authservice.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {


    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, TokenService tokenService, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public AuthenticationResponse register(UserCreateRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new UserAlreadyExistsException("User is already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        String jwt = jwtService.generateToken(user);
        tokenService.saveUserToken(jwt, user);

        return AuthenticationResponse.builder()
                .message("User is created")
                .token(jwt)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(()-> new UserNotFoundException("User not found"));
        String jwt = jwtService.generateToken(user);

        tokenService.revokeAllTokenByUser(user);
        tokenService.saveUserToken(jwt, user);


        return AuthenticationResponse.builder()
                .token(jwt)
                .message("User is logged in")
                .build();
    }
}
