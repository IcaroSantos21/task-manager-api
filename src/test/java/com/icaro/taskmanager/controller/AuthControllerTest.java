package com.icaro.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icaro.taskmanager.dto.AuthResponseDTO;
import com.icaro.taskmanager.dto.LoginRequestDTO;
import com.icaro.taskmanager.dto.RegisterRequestDTO;
import com.icaro.taskmanager.security.JwtUtil;
import com.icaro.taskmanager.security.SecurityConfig;
import com.icaro.taskmanager.security.UserDetailsServiceImpl;
import com.icaro.taskmanager.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void register_DocumentationRequest() throws Exception {
        var request = RegisterRequestDTO.builder()
                .name("Icaro")
                .email("icaro@gmail.com")
                .password("123456")
                .build();

        when(authService.register(any())).thenReturn(new AuthResponseDTO("fake_token"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(document("auth-register"));
    }

    @Test
    void login_DocumentationRequest() throws Exception {
        var request = LoginRequestDTO.builder()
                .email("icaro@gmail.com")
                .password("123456")
                .build();

        when(authService.login(any())).thenReturn(new AuthResponseDTO("fake_token"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("auth-login"));
    }
}