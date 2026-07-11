package com.icaro.taskmanager.service;

import com.icaro.taskmanager.dto.AuthResponseDTO;
import com.icaro.taskmanager.dto.LoginRequestDTO;
import com.icaro.taskmanager.dto.RegisterRequestDTO;
import com.icaro.taskmanager.model.Role;
import com.icaro.taskmanager.model.UserEntity;
import com.icaro.taskmanager.repository.UserRepository;
import com.icaro.taskmanager.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponseDTO register(RegisterRequestDTO requestDTO){
        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists in database");
        }

        var encoded = passwordEncoder.encode(requestDTO.getPassword());

        var user =  UserEntity.builder()
                .name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .password(encoded)
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);

        var token = jwtUtil.generateToken(requestDTO.getEmail());

        return new AuthResponseDTO(token);
    }

    public AuthResponseDTO login(LoginRequestDTO requestDTO) {
        var email = requestDTO.getEmail();
        var password = requestDTO.getPassword();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        var token = jwtUtil.generateToken(email);
        return new AuthResponseDTO(token);
    }
}
