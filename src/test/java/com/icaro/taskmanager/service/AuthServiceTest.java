package com.icaro.taskmanager.service;

import com.icaro.taskmanager.dto.LoginRequestDTO;
import com.icaro.taskmanager.dto.RegisterRequestDTO;
import com.icaro.taskmanager.model.Role;
import com.icaro.taskmanager.model.UserEntity;
import com.icaro.taskmanager.repository.UserRepository;
import com.icaro.taskmanager.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager manager;

    @InjectMocks
    AuthService authService;

    @Mock
    JwtUtil jwtUtil;

    RegisterRequestDTO registerRequestDTO;
    LoginRequestDTO loginRequestDTO;
    UserEntity entity;

    @BeforeEach
    void setUp() {
        registerRequestDTO = RegisterRequestDTO.builder()
                .name("Icaro")
                .email("icaro@gmail.com")
                .password("1234567")
                .build();

        loginRequestDTO = LoginRequestDTO.builder()
                .email("icaro@gmail.com")
                .password("123456")
                .build();

        entity = UserEntity.builder()
                .id(1L)
                .name("Icaro")
                .email("123456")
                .password("123456")
                .role(Role.ROLE_USER)
                .build();
    }

    @Test
    void register_ValidInput_ReturnsToken() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("123456");
        when(userRepository.save(any())).thenReturn(entity);
        when(jwtUtil.generateToken(anyString())).thenReturn("fake_token");

        var result = authService.register(registerRequestDTO);

        assertNotNull(result);
        assertEquals("fake_token", result.getToken());
    }

    @Test
    void register_EmailAlreadyExists_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(entity));

        assertThrows(RuntimeException.class, () -> authService.register(registerRequestDTO));
    }

    @Test
    void login_ValidCredentials_ReturnsToken() {

        when(manager.authenticate(any())).thenReturn(null);
        when(jwtUtil.generateToken(anyString())).thenReturn("fake_token");
        var result = authService.login(loginRequestDTO);

        assertNotNull(result);
        assertEquals("fake_token", result.getToken());
    }

    @Test
    void login_InvalidCredentials_ThrowsException() {
        when(manager.authenticate(any())).thenThrow(new RuntimeException("Invalid credentials"));

        assertThrows(RuntimeException.class, () -> authService.login(loginRequestDTO));
    }
}