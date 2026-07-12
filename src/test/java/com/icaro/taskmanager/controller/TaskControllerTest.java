package com.icaro.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icaro.taskmanager.dto.TaskRequestDTO;
import com.icaro.taskmanager.dto.TaskResponseDTO;
import com.icaro.taskmanager.model.TaskStatus;
import com.icaro.taskmanager.security.JwtUtil;
import com.icaro.taskmanager.security.SecurityConfig;
import com.icaro.taskmanager.security.UserDetailsServiceImpl;
import com.icaro.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
@Import(SecurityConfig.class)
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private JwtUtil jwtUtil;

    TaskResponseDTO responseDTO = new TaskResponseDTO();
    TaskRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        responseDTO.setId(1);
        responseDTO.setTitle("Fazer curso de arquitetura web");
        responseDTO.setDescription("Entrar na imersão gratuita da alura");
        responseDTO.setStatus(TaskStatus.PENDING);
        responseDTO.setCreatedAt(LocalDateTime.now());
        responseDTO.setUpdatedAt(LocalDateTime.now());

        requestDTO = TaskRequestDTO.builder()
                .title("Fazer curso de arquitetura web")
                .description("Entrar na imersão gratuita da alura")
                .status(TaskStatus.PENDING)
                .build();
    }

    @Test
    @WithMockUser
    void findAll_DocumentRequest() throws Exception {
        when(taskService.findAll()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andDo(document("tasks-list"));
    }

    @Test
    @WithMockUser
    void create_DocumentRequest() throws Exception {
        when(taskService.create(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andDo(document("tasks-create"));
    }
}