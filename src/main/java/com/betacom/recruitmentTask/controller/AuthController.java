package com.betacom.recruitmentTask.controller;

import com.betacom.recruitmentTask.dto.AuthRequest;
import com.betacom.recruitmentTask.dto.AuthResponse;
import com.betacom.recruitmentTask.model.Users;
import com.betacom.recruitmentTask.repository.UserRepository;
import com.betacom.recruitmentTask.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody AuthRequest request) {

        if (userRepository.findByLogin(request.getLogin()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Users newUser = new Users();
        newUser.setLogin(request.getLogin());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(newUser);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Optional<Users> user = userRepository.findByLogin(request.getLogin());

        if (user.isEmpty() || !passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtUtil.generateToken(user.get().getLogin());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
