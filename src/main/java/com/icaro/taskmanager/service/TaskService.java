package com.icaro.taskmanager.service;

import com.icaro.taskmanager.dto.TaskRequestDTO;
import com.icaro.taskmanager.dto.TaskResponseDTO;
import com.icaro.taskmanager.model.Task;
import com.icaro.taskmanager.model.TaskStatus;
import com.icaro.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor  // Lombok: injeta o repositório pelo construtor
public class TaskService {

    private final TaskRepository taskRepository;

    // Criando minha task, convertendo o DTO em Entity
    public TaskResponseDTO create(TaskRequestDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());

        // Aqui estou falando que SE não vier nenhum valor para status, ele deve ser PENDING
        task.setStatus(dto.getStatus() != null ? dto.getStatus() : TaskStatus.PENDING);

        // Aqui é onde acontece o insert do banco de dados, o JPA gerencia isso
        Task saved = taskRepository.save(task);
        return toResponseDTO(saved); // Aqui é onde acontece a conversão
    }

    // Aqui eu listo minha tabela
    public List<TaskResponseDTO> findAll() {
        return taskRepository.findAll()
                .stream() // busca no banco
                .map(this::toResponseDTO) // transformo em lista
                .collect(Collectors.toList()); // converte cada item
    }

    // Aqui eu estou buscando a task com base no id
    public TaskResponseDTO findById(Long id) {
        Task task = taskRepository.findById(id) // Busca no banco de dados
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id)); // Lançando o erro caso não encotre o id
        return toResponseDTO(task); // Retornando a task que encontrar
    }

    // Aqui eu estou atualizando uma tarefa na minha tabela
    public TaskResponseDTO update(Long id, TaskRequestDTO dto) {
        Task task = taskRepository.findById(id) // Busca no banco de dados
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id)); // Lançando a exceção caso não encontre a task

        // Atualizando os campos
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        if (dto.getStatus() != null) task.setStatus(dto.getStatus());

        return toResponseDTO(taskRepository.save(task)); // Salva novamente
    }

    // Deletando a task pelo id
    public void delete(Long id) {
        taskRepository.findById(id) // Procurando a task pelo id
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id)); // Lançando a exceção caso não tenha a tarefa
        taskRepository.deleteById(id); // Essa linha deleta a tarefa do meu banco de dados
    }

    // Buscando com base no status da tarefa
    public List<TaskResponseDTO> findByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status)
                .stream() // Busca no banco
                .map(this::toResponseDTO)
                .collect(Collectors.toList()); // Transforma em tudo em lista
    }

    // Metodo privado de conversão (pattern: Mapper manual)
    private TaskResponseDTO toResponseDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        return dto;
    }
}