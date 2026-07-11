package com.icaro.taskmanager.service;

import com.icaro.taskmanager.dto.TaskRequestDTO;
import com.icaro.taskmanager.model.Role;
import com.icaro.taskmanager.model.TaskEntity;
import com.icaro.taskmanager.model.TaskStatus;
import com.icaro.taskmanager.model.UserEntity;
import com.icaro.taskmanager.repository.TaskRepository;
import com.icaro.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    TaskService taskService;

    TaskRequestDTO dto;
    UserEntity user;
    TaskEntity entity;

    SecurityContext context;
    Authentication authentication;

    MockedStatic<SecurityContextHolder> securityContextHolder;

    @BeforeEach
    void setUp() {
        securityContextHolder = mockStatic(SecurityContextHolder.class);

        context = mock(SecurityContext.class);
        authentication = mock(Authentication.class);

        dto = TaskRequestDTO.builder()
            .title("Estudar Spring")
            .description("Aprender mais sobre o Framework Spring")
            .status(TaskStatus.PENDING)
            .build();

        user = UserEntity.builder()
            .id(1L)
            .name("Icaro")
            .email("icaro@gmail.com")
            .password("123456")
            .role(Role.ROLE_USER)
            .build();

        entity = TaskEntity.builder()
            .id(1L)
            .title(dto.getTitle())
            .description(dto.getDescription())
            .status(dto.getStatus())
            .createdAt(LocalDateTime.now())
            .user(user)
            .build();
    }

    @AfterEach
    void tearDown() {
        securityContextHolder.close();
    }

    @Test
    void create_ValidInput_ReturnsTaskResponseDTO() {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        when(taskRepository.save(any())).thenReturn(entity);

        mockSecurityContext("icaro@gmail.com");

        var result = taskService.create(dto);

        assertNotNull(result);
        assertEquals("Estudar Spring", result.getTitle());
        assertEquals(TaskStatus.PENDING, result.getStatus());
        verify(taskRepository, times(1)).save(any());
    }

    @Test
    void create_UserNotFound_ThrowsRunTimeException() {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        mockSecurityContext("icaro@gmail.com");

        assertThrows(RuntimeException.class, () -> taskService.create(dto));
    }

    @Test
    void findById_ValidId_ReturnsTaskResponseDTO() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(entity));

        mockSecurityContext("icaro@gmail.com");

        var result = taskService.findById(1L);

        assertNotNull(result);
        assertEquals("Estudar Spring", result.getTitle());
        assertEquals(TaskStatus.PENDING, result.getStatus());
    }

    @Test
    void findById_TaskNotFound_ThrowsRuntimeExceptions() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.findById(99L));
    }

    @Test
    void findById_AccessDenied_ThrowsRunTimeException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(entity));

        mockSecurityContext("joazinho@gmail.com");

        assertThrows(RuntimeException.class, () -> taskService.findById(1L));
    }

    @Test
    void findAll_ReturnUserTasks() {
        when(taskRepository.findByUserEmail(anyString())).thenReturn(List.of(entity));

        mockSecurityContext("icaro@gmail.com");

        var result = taskService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Estudar Spring", result.get(0).getTitle());
    }

    @Test
    void update_ValidInput_ReturnsUpdatedTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(entity));
        dto.setStatus(TaskStatus.IN_PROGRESS);
        entity.setStatus(dto.getStatus());
        when(taskRepository.save(any())).thenReturn(entity);
        mockSecurityContext("icaro@gmail.com");

        var result = taskService.update(1L, dto);

        assertNotNull(result);
        assertEquals("Estudar Spring", result.getTitle());
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
    }

    @Test
    void update_TaskNotFound_ThrowsRuntimeException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.update(1L, dto));
    }

    @Test
    void update_AccessDenied_ThrowsRuntimeException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(entity));

        mockSecurityContext("joazinho@gmail.com");

        assertThrows(RuntimeException.class, () -> taskService.update(1L, dto));
    }

    @Test
    void delete_ValidId_DeletesTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(entity));

        mockSecurityContext("icaro@gmail.com");

        taskService.delete(1L);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void delete_TaskNotFound_ThrowsRuntimeException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.delete(1L));
    }

    @Test
    void delete_AccessDenied_ThrowsRuntimeExceptions() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(entity));

        mockSecurityContext("joazinho@gmail.com");

        assertThrows(RuntimeException.class, () -> taskService.delete(1L));
    }

    private void mockSecurityContext(String email) {
        when(SecurityContextHolder.getContext()).thenReturn(context);
        when(context.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getName()).thenReturn(email);
    }
}