package com.icaro.taskmanager.service;

import com.icaro.taskmanager.dto.TaskRequestDTO;
import com.icaro.taskmanager.dto.TaskResponseDTO;
import com.icaro.taskmanager.model.TaskEntity;
import com.icaro.taskmanager.model.TaskStatus;
import com.icaro.taskmanager.repository.TaskRepository;
import com.icaro.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor  // Lombok: injeta o repositório pelo construtor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskResponseDTO create(TaskRequestDTO dto) {
        var user = userRepository.findByEmail(getAuthenticatedEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        var task = TaskEntity.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus() != null ? dto.getStatus() : TaskStatus.PENDING)
                .user(user)
                .build();
        var saved = taskRepository.save(task);
        return toResponseDTO(saved);
    }

    public List<TaskResponseDTO> findAll() {
        return taskRepository.findByUserEmail(getAuthenticatedEmail())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    public TaskResponseDTO findById(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        if (!task.getUser().getEmail().equals(getAuthenticatedEmail())) {
            throw new RuntimeException("Access denied");
        }
        return toResponseDTO(task);
    }


    public TaskResponseDTO update(Long id, TaskRequestDTO dto) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        if (!task.getUser().getEmail().equals(getAuthenticatedEmail())) {
            throw new RuntimeException("Access denied");
        }

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        if (dto.getStatus() != null) task.setStatus(dto.getStatus());

        return toResponseDTO(taskRepository.save(task));
    }


    public void delete(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        if (!task.getUser().getEmail().equals(getAuthenticatedEmail())) {
            throw new RuntimeException("Access denied");
        }
        taskRepository.deleteById(id);
    }

    public List<TaskResponseDTO> findByStatus(TaskStatus status) {
        return taskRepository.findByUserEmailAndStatus(getAuthenticatedEmail(), status)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private TaskResponseDTO toResponseDTO(TaskEntity taskEntity) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(taskEntity.getId());
        dto.setTitle(taskEntity.getTitle());
        dto.setDescription(taskEntity.getDescription());
        dto.setStatus(taskEntity.getStatus());
        dto.setCreatedAt(taskEntity.getCreatedAt());
        dto.setUpdatedAt(taskEntity.getUpdatedAt());
        return dto;
    }

    private String getAuthenticatedEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}