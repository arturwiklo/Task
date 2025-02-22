package com.betacom.recruitmentTask.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        jwtUtil.setSecretKey("01234567890123456789012345678901");
    }

    @Test
    void shouldGenerateAndValidateToken() {
        String token = jwtUtil.generateToken("testUser");
        assertNotNull(token);

        String username = jwtUtil.extractUsername(token);
        assertEquals("testUser", username);

        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void shouldInvalidateExpiredToken() throws InterruptedException {
        jwtUtil.setExpirationTime(1000);

        String expiredToken = jwtUtil.generateToken("testUser");
        Thread.sleep(2000);

        assertFalse(jwtUtil.validateToken(expiredToken), "The token should be invalid after the time has elapsed");
    }

    @Test
    void shouldRejectInvalidToken() {
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImlhdCI6MTY5NDgxMjM4OX0.invalidsignature";

        assertFalse(jwtUtil.validateToken(invalidToken), "Invalid token should be rejected");
    }
}
