package com.example.authservice.service.impl;

import com.example.authservice.exception.user.UserNotFoundException;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Username : {}", username);

        UserDetails userDetails = userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException("Not found"));


        log.info("User Details username : {}", userDetails.getUsername());
        return userDetails;
    }
}
