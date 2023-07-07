package com.authservice;

import com.authservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceTest {

    private static final String TEST_SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateToken_ValidToken_NoExceptionThrown() {
        String token = generateToken("testuser");
        assertDoesNotThrow(() -> jwtService.validateToken(token));
    }



    @Test
    void validateToken_InvalidToken_ThrowsMalformedJwtException() {
        String invalidToken = "invalid_token";
        assertThrows(MalformedJwtException.class, () -> jwtService.validateToken(invalidToken));
    }



    private String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return jwtService.createToken(claims, username);
    }

    private String generateTokenWithExpiration(String username) {
        Map<String, Object> claims = new HashMap<>();
        return jwtService.createToken(claims, username);
    }

    private boolean isValidToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(TEST_SECRET.getBytes());
            Jws<Claims> claimsJws = io.jsonwebtoken.Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
