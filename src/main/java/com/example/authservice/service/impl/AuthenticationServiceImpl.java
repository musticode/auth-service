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
import com.example.authservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {


    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userService;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheTokenService cacheTokenService;

    public AuthenticationServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, TokenService tokenService, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserServiceImpl userService, StringRedisTemplate stringRedisTemplate, RedisTemplate<String, Object> redisTemplate, CacheTokenService cacheTokenService/*, RedisTemplate<String, Object> redisTemplate*/) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
//        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisTemplate = redisTemplate;
        this.cacheTokenService = cacheTokenService;
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
//        /**
//         * username üzerinden user var mı diye bak
//         * user varsa cache de jwt var mı diye bak
//         * cache varsa ordan al
//         * cache'de yoksa yeni token yarat, cache'e at
//         * */
//
//        User foundUser = userService.findUserWithUsername(request.getUsername());
//        if (foundUser.isCredentialsNonExpired()){
//            final String cachedToken = cacheTokenService.getCachedToken(request.getUsername());
//            if (!cachedToken.isEmpty()){
//                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
//            }else {
//                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
//                final String jwt = jwtService.generateToken(foundUser);
//                cacheToken(request.getUsername(), jwt);
//            }
//        }
//


//        final String cachedToken = getCachedToken(request.getUsername());
//        if (cachedToken.isEmpty()){
//            cacheToken(request.getUsername(), jwtService.generateToken(user));
//        }


        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userService.findUserWithUsername(request.getUsername());
        String jwt = jwtService.generateToken(user);
        tokenService.revokeAllTokenByUser(user);
        tokenService.saveUserToken(jwt, user);

//        cacheToken(request.getUsername(), jwt);
//        String cached = getCachedToken(request.getUsername());
//        System.out.println("caccccheeddd  "  + cached);
//
//
//        saveData(request.getUsername(), jwt);
//        log.info("tstttt : {}", getData(request.getUsername()));

        return AuthenticationResponse.builder()
                .token(jwt)
                .message("User is logged in")
                .build();

        //     @Cacheable(cacheNames = "movie", key = "'movie#' + #id")
    }

    @Override
    public String logout() {

        return null;
    }


    private void cacheToken(String username, String token) {
        // Cache the token in Redis with a TTL (e.g., 60 minutes)
        stringRedisTemplate.opsForValue().set("token:" + username, token, Duration.ofMinutes(60));
    }

    private String getCachedToken(String username) {
        // Retrieve the token from Redis cache
        return stringRedisTemplate.opsForValue().get("token:" + username);
    }


    public void saveData(String key, Object data) {
        redisTemplate.opsForValue().set(key, data);
    }

    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
