package com.authservice.controller;

import com.authservice.entity.UserCredential;
import com.authservice.exception.UserNotFoundException;
import com.authservice.repository.UserCredentialRepository;
import com.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService service;

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @PostMapping
    public ResponseEntity<UserCredential> addUser(@Valid @RequestBody UserCredential user, BindingResult bindingResult) {
        logger.info("Received addUser request for username: {}", user.getUsername());
        UserCredential newUser = service.addUser(user, bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestBody UserCredential userCredential) {
        logger.info("Received getToken request for username: {}", userCredential.getUsername());
        UserCredential user = userCredentialRepository.findByUsername(userCredential.getUsername());

        if (user.getUsername().equals(userCredential.getUsername()) && user.getPassword().equals(userCredential.getPassword())) {
            String token = service.generateToken(userCredential.getUsername());
            return ResponseEntity.ok(token);
        } else {
            throw new RuntimeException("Invalid access");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserCredential> login(@RequestBody UserCredential userCredential) {
        logger.info("Received login request for username: {}", userCredential.getUsername());
        String username = userCredential.getUsername();
        UserCredential user = userCredentialRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("No user found with username: " + username);
        }

        if (user.getUsername().equals(userCredential.getUsername()) && user.getPassword().equals(userCredential.getPassword())) {
            String token = service.generateToken(userCredential.getUsername());
            user.setToken(token);
            return ResponseEntity.ok(user);
        } else {
            throw new RuntimeException("Invalid access");
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
        logger.info("Received validateToken request for token: {}", token);
        service.validateToken(token);
        return ResponseEntity.ok("Token is valid");
    }
}
