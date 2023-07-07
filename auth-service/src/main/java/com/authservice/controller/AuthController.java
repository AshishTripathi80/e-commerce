package com.authservice.controller;

import com.authservice.entity.UserCredential;
import com.authservice.exceptions.UserNotFoundException;
import com.authservice.repository.UserCredentialRepository;
import com.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService service;


    @Autowired
    private UserCredentialRepository userCredentialRepository;


    @PostMapping
    public UserCredential addUser(@Valid @RequestBody UserCredential user, BindingResult bindingResult) {
        return service.addUser(user,bindingResult);
    }

    @PostMapping("/token")
    public String getToken(@RequestBody UserCredential userCredential) {
        UserCredential user = userCredentialRepository.findByUsername(userCredential.getUsername());


        if (user.getUsername().equals(userCredential.getUsername()) && user.getPassword().equals(userCredential.getPassword())) {

            return service.generateToken(userCredential.getUsername());
        } else {
            throw new RuntimeException("invalid access");
        }
    }

    @PostMapping("/login")
    public UserCredential login(@RequestBody UserCredential userCredential) {
        String username=userCredential.getUsername();
        UserCredential user = userCredentialRepository.findByUsername(username);
        if(user==null){
            throw new UserNotFoundException("No user found with username : "+username);
        }


        if (user.getUsername().equals(userCredential.getUsername()) && user.getPassword().equals(userCredential.getPassword())) {
            String token= service.generateToken(userCredential.getUsername());
            user.setToken(token);
            return user;
        } else {
            throw new RuntimeException("invalid access");
        }
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        service.validateToken(token);
        return "Token is valid";
    }

}
