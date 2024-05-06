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
import com.example.authservice.service.event.RedisMessagePublisher;
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

import static com.example.authservice.service.event.RedisMessageHandler.messageList;

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
    private final RedisMessagePublisher redisMessagePublisher;

    public AuthenticationServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, TokenService tokenService, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserServiceImpl userService, StringRedisTemplate stringRedisTemplate, RedisTemplate<String, Object> redisTemplate, CacheTokenService cacheTokenService, RedisMessagePublisher redisMessagePublisher/*, RedisTemplate<String, Object> redisTemplate*/) {
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
        this.redisMessagePublisher = redisMessagePublisher;
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
        String  jwt = "";
        User foundUser = userService.findUserWithUsername(request.getUsername());
        if (foundUser.isCredentialsNonExpired()){
            final String cachedToken = cacheTokenService.getCachedToken(request.getUsername());

            if (cachedToken != null){
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
                jwt = cacheTokenService.getCachedToken(request.getUsername());
                log.info("Cached token : {}", jwt);
            }else {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
                jwt = jwtService.generateToken(foundUser);
                cacheTokenService.cacheToken(request.getUsername(), jwt);
            }
        }



//        final String cachedToken = getCachedToken(request.getUsername());
//        if (cachedToken.isEmpty()){
//            cacheToken(request.getUsername(), jwtService.generateToken(user));
//        }


        /*
        // authentication without cache

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userService.findUserWithUsername(request.getUsername());
        String jwt = jwtService.generateToken(user);
        tokenService.revokeAllTokenByUser(user);
        tokenService.saveUserToken(jwt, user);
         */



//        cacheToken(request.getUsername(), jwt);
//        String cached = getCachedToken(request.getUsername());
//        System.out.println("caccccheeddd  "  + cached);
//
//
//        saveData(request.getUsername(), jwt);
//        log.info("tstttt : {}", getData(request.getUsername()));

        redisMessagePublisher.publish("DÜRRRÜKKK");
        messageList.forEach(System.out::println);
        return AuthenticationResponse.builder()
                .token(jwt)
                .message("User is logged in")
                .build();

    }

    @Override
    public String logout() {
        return null;
    }

}
