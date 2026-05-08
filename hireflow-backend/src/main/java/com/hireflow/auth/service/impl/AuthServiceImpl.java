package com.hireflow.auth.service.impl;

import com.hireflow.auth.dto.request.LoginRequest;
import com.hireflow.auth.dto.request.RegisterRequest;
import com.hireflow.auth.dto.response.LoginResponse;
import com.hireflow.auth.dto.response.RegisterResponse;
import com.hireflow.auth.service.AuthService;
import com.hireflow.common.entity.User;
import com.hireflow.common.exception.custom.DuplicateResourceException;
import com.hireflow.common.exception.custom.InvalidCredentialsException;
import com.hireflow.common.repository.UserRepository;
import com.hireflow.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {

        String email = request.getEmail().trim();
        String password = request.getPassword().trim();

        log.info("LOGIN ATTEMPT EMAIL: [{}]", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid credentials"));

        boolean isMatch = passwordEncoder.matches(password, user.getPassword());

        log.info("PASSWORD MATCH RESULT: {}", isMatch);

        if (!isMatch) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return LoginResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .token(token)
                .role(user.getRole().name())
                .build();
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {

        log.info("Registration request received for email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {

            log.error("Registration failed. Email already exists: {}", request.getEmail());

            throw new DuplicateResourceException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail().trim())
                .password(passwordEncoder.encode(request.getPassword().trim()))
                .role(request.getRole())
                .build();

        User savedUser = userRepository.save(user);

        log.info("User registered successfully with id: {} and role: {}",
                savedUser.getId(),
                savedUser.getRole());

        return RegisterResponse.builder()
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .build();
    }
}