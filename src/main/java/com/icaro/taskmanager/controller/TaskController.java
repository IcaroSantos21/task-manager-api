package com.icaro.taskmanager.controller;

import com.icaro.taskmanager.dto.TaskRequestDTO;
import com.icaro.taskmanager.dto.TaskResponseDTO;
import com.icaro.taskmanager.model.TaskStatus;
import com.icaro.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks") // Definindo a rota base
@RequiredArgsConstructor // Criando automaticamente o construtor como final
public class TaskController {

    // Referencia do serviço que contém a lógica de negócio
    private final TaskService taskService;

    // Aqui eu estou recebendo o JSON do cliente, validando e criando minha nova tarefa
    // POST /api/tasks
    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@Valid @RequestBody TaskRequestDTO dto) {
        TaskResponseDTO response = taskService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // Aqui eu estou retornando o status HTTP com o corpo da resposta contento o objeto
    }

    // Aqui eu estou buscando todas as tarefas que tem dentro do meu banco de dados
    // GET /api/tasks
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> findAll() {
        return ResponseEntity.ok(taskService.findAll()); // Retorna o status com a lista
    }

    // Aqui eu estou buscando a tarefa no meu banco de dados com base no ID dele
    // GET /api/tasks/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id)); // Retorna o status com a tarefa que eu busquei
    }

    // Aqui eu estou atualizando uma tarefa existente, procurando ela pelo ID
    // PUT /api/tasks/{id}
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(@PathVariable Long id,
                                                  @Valid @RequestBody TaskRequestDTO dto) {
        return ResponseEntity.ok(taskService.update(id, dto)); // Aqui eu estou retornando o status com a tarefa atualizada
    }

    // Aqui eu estou atualizando uma tarefa existente, procurando pelo id
    // DELETE /api/tasks/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id); // Chama o service para deletar a tarefa
        return ResponseEntity.noContent().build(); // retorna o status
    }

    // Aqui eu busco as tarefas com base nos status
    // GET /api/tasks/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponseDTO>> findByStatus(@PathVariable TaskStatus status) {
        return ResponseEntity.ok(taskService.findByStatus(status)); // retorna os status a lista das tarefas filtrada com base nos status
    }
}