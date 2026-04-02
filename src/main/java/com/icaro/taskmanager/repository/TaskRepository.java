package com.icaro.taskmanager.repository;

import com.icaro.taskmanager.model.Task;
import com.icaro.taskmanager.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
}
