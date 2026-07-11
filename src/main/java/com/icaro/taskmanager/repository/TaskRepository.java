package com.icaro.taskmanager.repository;

import com.icaro.taskmanager.model.TaskEntity;
import com.icaro.taskmanager.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByStatus(TaskStatus status);

    List<TaskEntity> findByUserEmail(String email);

    List<TaskEntity> findByUserEmailAndStatus(String email, TaskStatus status);
}
