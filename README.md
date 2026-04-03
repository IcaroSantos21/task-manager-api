# 📝 Task Manager API

> REST API para gerenciamento de tarefas, desenvolvida com Java e Spring Boot.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.13-green?style=flat-square&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)
![Maven](https://img.shields.io/badge/Maven-3.9-red?style=flat-square&logo=apachemaven)
![Status](https://img.shields.io/badge/Status-Conclu%C3%ADdo-brightgreen?style=flat-square)

---

## 📌 Sobre o projeto

API RESTful para criação e gerenciamento de tarefas com controle de status. O projeto foi desenvolvido com foco em boas práticas de desenvolvimento backend, seguindo arquitetura em camadas, design patterns e versionamento com Git Flow.

---

## 🚀 Funcionalidades

- ✅ Criar tarefa
- ✅ Listar todas as tarefas
- ✅ Buscar tarefa por ID
- ✅ Atualizar tarefa
- ✅ Deletar tarefa
- ✅ Filtrar tarefas por status (`PENDING`, `IN_PROGRESS`, `COMPLETED`)
- ✅ Validação de campos obrigatórios
- ✅ Tratamento global de erros

---

## 🛠️ Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 17 |
| Spring Boot | 3.5.13 |
| Spring Data JPA | 3.5.13 |
| MySQL | 8.0 |
| Lombok | latest |
| Maven | 3.9 |

---

## 🏗️ Arquitetura

O projeto segue o padrão **Layered Architecture** com separação de responsabilidades:

```
src/main/java/com/icaro/taskmanager/
├── controller/        → Recebe e responde requisições HTTP
├── service/           → Regras de negócio
├── repository/        → Acesso ao banco de dados
├── model/             → Entidades JPA
├── dto/               → Objetos de transferência de dados
└── exception/         → Tratamento global de erros
```

---

## ⚙️ Como rodar o projeto

### Pré-requisitos
- Java 17+
- MySQL 8.0+
- Maven

### 1. Clone o repositório
```bash
git clone https://github.com/IcaroSantos21/task-manager-api.git
cd task-manager-api
```

### 2. Crie o banco de dados
```sql
CREATE DATABASE taskmanager_db;
```

### 3. Configure o `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taskmanager_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 4. Rode a aplicação
```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`

---

## 📡 Endpoints

### Base URL: `/api/tasks`

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/tasks` | Criar tarefa |
| `GET` | `/api/tasks` | Listar todas as tarefas |
| `GET` | `/api/tasks/{id}` | Buscar tarefa por ID |
| `PUT` | `/api/tasks/{id}` | Atualizar tarefa |
| `DELETE` | `/api/tasks/{id}` | Deletar tarefa |
| `GET` | `/api/tasks/status/{status}` | Filtrar por status |

---

### 📥 Exemplos de requisição

**Criar tarefa** — `POST /api/tasks`
```json
{
  "title": "Estudar Spring Boot",
  "description": "Estudar sobre injeção de dependência e JPA",
  "status": "IN_PROGRESS"
}
```

**Resposta** — `201 Created`
```json
{
  "id": 1,
  "title": "Estudar Spring Boot",
  "description": "Estudar sobre injeção de dependência e JPA",
  "status": "IN_PROGRESS",
  "createdAt": "2026-04-03T12:31:18",
  "updatedAt": "2026-04-03T12:31:18"
}
```

**Atualizar status** — `PUT /api/tasks/1`
```json
{
  "title": "Estudar Spring Boot",
  "description": "Estudar sobre injeção de dependência e JPA",
  "status": "COMPLETED"
}
```

**Filtrar por status** — `GET /api/tasks/status/PENDING`
```json
[
  {
    "id": 2,
    "title": "Criar testes unitários",
    "description": null,
    "status": "PENDING",
    "createdAt": "2026-04-03T13:00:00",
    "updatedAt": "2026-04-03T13:00:00"
  }
]
```

---

## 📊 Status das tarefas

```
PENDING      → Tarefa criada, aguardando início
IN_PROGRESS  → Tarefa em andamento
COMPLETED    → Tarefa concluída
```

---

## 🌿 Git Flow utilizado

```
main
 └── feature/task-entity
 └── feature/task-service-dto
 └── feature/task-controller
```

Cada funcionalidade foi desenvolvida em uma branch separada e integrada à `main` via Pull Request.

---

## 👨‍💻 Autor

**Ícaro Rodrigues Santos**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-icarorod21-blue?style=flat-square&logo=linkedin)](https://www.linkedin.com/in/icarorod21/)
[![GitHub](https://img.shields.io/badge/GitHub-IcaroSantos21-black?style=flat-square&logo=github)](https://github.com/IcaroSantos21)