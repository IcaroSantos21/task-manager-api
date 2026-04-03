package com.icaro.taskmanager.dto;

import com.icaro.taskmanager.model.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

// Aqui é o que eu devolvo para o meu usuário
@Data
public class TaskResponseDTO {
    private long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
