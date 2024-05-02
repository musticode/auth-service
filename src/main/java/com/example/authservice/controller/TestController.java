package com.example.authservice.controller;

import com.example.authservice.dto.AuthenticationResponse;
import com.example.authservice.dto.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public ResponseEntity<String> register(){
        return new ResponseEntity<>("Selam test", HttpStatus.OK);
    }

    @GetMapping("/demo")
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok("Hello from secured url");
    }

}
