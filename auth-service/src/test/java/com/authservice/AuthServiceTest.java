package com.authservice;

import com.authservice.entity.UserCredential;
import com.authservice.exception.InvalidUserDataException;
import com.authservice.repository.UserCredentialRepository;
import com.authservice.service.AuthService;
import com.authservice.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserCredentialRepository repository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addUser_ValidCredential_SuccessfullySaved() {
        UserCredential credential = new UserCredential();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(repository.save(credential)).thenReturn(credential);

        UserCredential savedCredential = authService.addUser(credential, bindingResult);

        assertNotNull(savedCredential);
        verify(repository, times(1)).save(credential);
    }

    @Test
    void addUser_InvalidCredential_ThrowsInvalidUserDataException() {
        UserCredential credential = new UserCredential();
        when(bindingResult.hasErrors()).thenReturn(true);
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("userCredential", "username", "Username is required."));
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        InvalidUserDataException exception = assertThrows(InvalidUserDataException.class, () -> {
            authService.addUser(credential, bindingResult);
        });

        assertEquals("Validation Failed!", exception.getMessage());
        assertEquals(1, exception.getErrors().size());
        assertEquals("username: Username is required.", exception.getErrors().get(0));
        verify(repository, never()).save(any(UserCredential.class));
    }

    @Test
    void generateToken_ValidUsername_ReturnsToken() {
        String username = "testuser";
        String expectedToken = "generated_token";
        when(jwtService.generateToken(username)).thenReturn(expectedToken);

        String token = authService.generateToken(username);

        assertNotNull(token);
        assertEquals(expectedToken, token);
        verify(jwtService, times(1)).generateToken(username);
    }

    @Test
    void validateToken_ValidToken_NoExceptionThrown() {
        String token = "valid_token";
        doNothing().when(jwtService).validateToken(token);

        assertDoesNotThrow(() -> authService.validateToken(token));

        verify(jwtService, times(1)).validateToken(token);
    }
}
