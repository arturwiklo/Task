package com.betacom.recruitmentTask.controller;

import com.betacom.recruitmentTask.dto.AuthRequest;
import com.betacom.recruitmentTask.model.Users;
import com.betacom.recruitmentTask.repository.UserRepository;
import com.betacom.recruitmentTask.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        authRequest = new AuthRequest();
        authRequest.setLogin("testUser");
        authRequest.setPassword("password123");
    }

    @Test
    void shouldReturn204WhenUserRegisteredSuccessfully() {
        when(userRepository.findByLogin(authRequest.getLogin())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(authRequest.getPassword())).thenReturn("hashedPassword");

        ResponseEntity<Void> response = authController.register(authRequest);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userRepository, times(1)).save(any(Users.class));
    }

    @Test
    void shouldReturn409WhenUserAlreadyExists() {
        when(userRepository.findByLogin(authRequest.getLogin())).thenReturn(Optional.of(new Users()));

        ResponseEntity<Void> response = authController.register(authRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    void shouldReturnTokenWhenLoginSuccessful() {
        Users user = new Users();
        user.setLogin(authRequest.getLogin());
        user.setPassword("hashedPassword");

        when(userRepository.findByLogin(authRequest.getLogin())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(authRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(authRequest.getLogin())).thenReturn("jwt_token");

        ResponseEntity<?> response = authController.login(authRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldReturn401WhenLoginFails() {
        when(userRepository.findByLogin(authRequest.getLogin())).thenReturn(Optional.empty());

        ResponseEntity<?> response = authController.login(authRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
