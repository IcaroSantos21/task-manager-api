package com.icaro.taskmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data // Gerar automaticamente os getters, setters
@NoArgsConstructor // Construtor sem argumentos
@AllArgsConstructor // Construtor com todos os atributos
@Entity // Defini como uma classe persistente
@Table(name = "tasks") // Defini o nome da tabela
public class Task {
    @Id // gerando chave primária, o banco gera automaticamente o valor
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Title is required") // Java não aceita vazio, nula, ou só espaços
    @Column(nullable = false) // Banco não aceita null
    private String title;

    @Column(columnDefinition = "TEXT") // Defini o tipo de valor que o banco aceita como TEXT
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.PENDING; // Toda task já vai começar pendente

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Quando a tarefa for criada defini o createdAt e updatedAt com o horário atual
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    // Quando tarefa for alterada defini o updatedAt com o horário atual
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
