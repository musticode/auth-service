package com.example.authservice.config;

import com.example.authservice.model.Token;
import com.example.authservice.repository.TokenRepository;
import com.example.authservice.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenService tokenService;

    public CustomLogoutHandler(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String authHeader = request.getHeader("Authorization");
        if (authentication != null || !authHeader.startsWith("Bearer ")){
            return;
        }

        String token = authHeader.substring(7);
        Token storedToken = tokenService.findToken(token);
        if (storedToken != null){
            storedToken.setLoggedOut(true);
            tokenService.saveToken(storedToken);
        }


    }
}
