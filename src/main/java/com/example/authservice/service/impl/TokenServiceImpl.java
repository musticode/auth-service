package com.example.authservice.service.impl;

import com.example.authservice.exception.token.TokenNotFoundException;
import com.example.authservice.model.Token;
import com.example.authservice.model.User;
import com.example.authservice.repository.TokenRepository;
import com.example.authservice.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Token findToken(String token) {
        return tokenRepository.findByToken(token).orElseThrow(()-> new TokenNotFoundException("Not found"));
    }

    @Override
    public Token findTokenWithId(long id) {
        return tokenRepository.findById(id).orElseThrow(()-> new TokenNotFoundException("Not found with id : " + id));
    }

    @Override
    public void saveToken(Token token) {
        if (token != null){
            tokenRepository.save(token);
            log.info("Token is saved {}", token);
        }
    }

    @Override
    public void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    @Override
    public void revokeAllTokenByUser(User user) {

        List<Token> userTokens = tokenRepository.findByUser(user);

        if (!userTokens.isEmpty()){
            log.info("User token list is not empty");

            userTokens.forEach(token -> {
                if (token.isLoggedOut()){
                    token.setLoggedOut(false);
                    log.info("User's user: {} is logged out, token : {}", user,token);
                }
            });

            saveAllTokens(userTokens);


        }







        //List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());

//        if (validTokens.isEmpty()){
//            return;
//        }
//
//        validTokens.forEach(token -> token.setLoggedOut(true));
//        tokenRepository.saveAll(validTokens);

    }

    private void saveAllTokens(List<Token> tokens){
        tokenRepository.saveAll(tokens);
    }

}
