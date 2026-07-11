package com.icaro.taskmanager.dto;

import com.icaro.taskmanager.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private TaskStatus status;
}
