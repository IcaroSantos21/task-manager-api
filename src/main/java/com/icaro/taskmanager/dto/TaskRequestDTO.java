package com.icaro.taskmanager.dto;

import com.icaro.taskmanager.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// Aqui é onde eu pego o que o meu usuário mandar na requisição
@Data
public class TaskRequestDTO {
    @NotBlank(message = "Title is required") // Aqui eu valido e garanto que não terá nenhum valor vazio
    private String title;

    private String description;

    private TaskStatus status; // Aqui eu estou permitindo que o usuário mande um status para a task
    // porém não estou tornando algo obrigatório
}
