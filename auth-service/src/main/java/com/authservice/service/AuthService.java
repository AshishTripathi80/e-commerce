package com.authservice.service;

import com.authservice.entity.UserCredential;
import com.authservice.exceptions.InvalidUserDataException;
import com.authservice.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private UserCredentialRepository repository;

    @Autowired
    private JwtService jwtService;

    public UserCredential addUser(UserCredential credential, BindingResult bindingResult) {

        validationError(bindingResult);

        return  repository.save(credential);
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }



    /**
     * Handle validation errors.
     *
     * @param bindingResult The BindingResult object containing validation errors.
     * @throws InvalidUserDataException if validation errors are present.
     */
    public void validationError(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<String> errors = new ArrayList<>();

            // Convert each field error to a string representation
            for (FieldError fieldError : fieldErrors) {
                errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            }

            // Throw an exception with the validation errors
            throw new InvalidUserDataException("Validation Failed!", LocalDateTime.now(), errors);
        }
    }


}
