package com.icaro.taskmanager.controller;

import com.icaro.taskmanager.dto.AuthResponseDTO;
import com.icaro.taskmanager.dto.LoginRequestDTO;
import com.icaro.taskmanager.dto.RegisterRequestDTO;
import com.icaro.taskmanager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterRequestDTO requestDTO) {
        var response = authService.register(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO requestDTO) {
        var response = authService.login(requestDTO);

        return ResponseEntity.ok(response);
    }
}
