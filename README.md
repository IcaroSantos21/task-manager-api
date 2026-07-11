# 📝 Task Manager API

> API REST completa para gerenciamento de tarefas com autenticação JWT, interface web responsiva e deploy em produção.

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.13-green?style=flat-square&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-blue?style=flat-square&logo=postgresql)
![Flyway](https://img.shields.io/badge/Flyway-Migrations-CC0200?style=flat-square&logo=flyway)
![JWT](https://img.shields.io/badge/Auth-JWT-black?style=flat-square&logo=jsonwebtokens)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?style=flat-square&logo=docker)
![Maven](https://img.shields.io/badge/Maven-3.9-red?style=flat-square&logo=apachemaven)
![Deploy](https://img.shields.io/badge/Deploy-Render-46E3B7?style=flat-square&logo=render)

🔗 **[Ver aplicação em produção](https://task-manager-api-k716.onrender.com)**

---

## 📌 Sobre o projeto

API RESTful para criação e gerenciamento de tarefas pessoais, com autenticação via **JWT** e isolamento de dados por usuário. Cada usuário gerencia apenas as suas próprias tarefas — nenhum dado vaza entre contas.

O projeto foi desenvolvido com foco em boas práticas de backend: arquitetura em camadas, DTOs, tratamento global de erros, versionamento de schema com Flyway, containerização com Docker e deploy contínuo no Render.

Conta também com uma **interface web responsiva** servida pela própria API, com suporte a dark/light mode e navegação mobile.

---

## 🚀 Funcionalidades

- ✅ Cadastro e login de usuários com JWT
- ✅ Senhas criptografadas com BCrypt
- ✅ Perfis de usuário (`ROLE_USER`, `ROLE_ADMIN`)
- ✅ Tarefas isoladas por usuário autenticado
- ✅ CRUD completo de tarefas
- ✅ Filtro por status (`PENDING`, `IN_PROGRESS`, `COMPLETED`)
- ✅ Validação de campos com Bean Validation
- ✅ Tratamento global de erros
- ✅ Migrations versionadas com Flyway
- ✅ Interface web responsiva com dark/light mode
- ✅ Deploy contínuo no Render

---

## 🛠️ Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.13 |
| Spring Security | 3.5.13 |
| Spring Data JPA | 3.5.13 |
| PostgreSQL | 18 |
| Flyway | Core + PostgreSQL |
| JJWT | 0.12.6 |
| Lombok | latest |
| Maven | 3.9 |
| Docker | multi-stage build |

---

## 🏗️ Arquitetura

O projeto segue o padrão **Layered Architecture** com separação clara de responsabilidades:

```
src/main/java/com/icaro/taskmanager/
├── controller/        → Recebe e responde requisições HTTP
├── service/           → Regras de negócio
├── repository/        → Acesso ao banco de dados (Spring Data JPA)
├── model/             → Entidades JPA (UserEntity, TaskEntity, Role, TaskStatus)
├── dto/               → Objetos de transferência de dados
├── security/          → JWT, filtro de autenticação e configuração do Spring Security
└── exception/         → Tratamento global de erros

src/main/resources/
├── db/migration/      → Scripts Flyway (V1, V2, V3)
└── static/            → Interface web (HTML, CSS, JS separados)
```

---

## ⚙️ Como rodar localmente

### Pré-requisitos
- Java 21+
- PostgreSQL
- Maven (ou use o wrapper `./mvnw`)

### 1. Clone o repositório
```bash
git clone https://github.com/IcaroSantos21/task-manager-api.git
cd task-manager-api
```

### 2. Configure as variáveis de ambiente

```bash
DATABASE_URL=jdbc:postgresql://localhost:5432/taskmanager_db
DB_USER=seu_usuario
DB_PASSWORD=sua_senha
JWT_SECRET=chave_base64_minimo_32_bytes
PORT=8080
```

> Gere o JWT_SECRET com: `openssl rand -base64 32`

> As tabelas são criadas automaticamente pelo Flyway na primeira execução.

### 3. Rode a aplicação
```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`

### 4. (Opcional) Com Docker
```bash
docker build -t task-manager-api .
docker run -p 8080:8080 \
  -e DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/taskmanager_db \
  -e DB_USER=seu_usuario \
  -e DB_PASSWORD=sua_senha \
  -e JWT_SECRET=sua_chave_base64 \
  task-manager-api
```

---

## 🔐 Autenticação

Todas as rotas de tarefas exigem token JWT válido no header `Authorization`.

### Registrar — `POST /auth/register`
```json
{
  "name": "Ícaro Santos",
  "email": "icaro@email.com",
  "password": "senha123"
}
```

**Resposta** `201 Created`
```json
{ "token": "eyJhbGciOiJIUzI1NiJ9..." }
```

### Login — `POST /auth/login`
```json
{
  "email": "icaro@email.com",
  "password": "senha123"
}
```

**Resposta** `200 OK`
```json
{ "token": "eyJhbGciOiJIUzI1NiJ9..." }
```

### Usando o token

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## 📡 Endpoints

### Autenticação — `/auth`

| Método | Endpoint | Auth | Descrição |
|---|---|---|---|
| `POST` | `/auth/register` | Pública | Cria novo usuário |
| `POST` | `/auth/login` | Pública | Autentica e retorna token |

### Tarefas — `/tasks`

| Método | Endpoint | Auth | Descrição |
|---|---|---|---|
| `POST` | `/tasks` | JWT | Criar tarefa |
| `GET` | `/tasks` | JWT | Listar tarefas do usuário |
| `GET` | `/tasks/{id}` | JWT | Buscar tarefa por ID |
| `PUT` | `/tasks/{id}` | JWT | Atualizar tarefa |
| `DELETE` | `/tasks/{id}` | JWT | Deletar tarefa |
| `GET` | `/tasks/status/{status}` | JWT | Filtrar por status |

---

### Exemplos de requisição

**Criar tarefa** — `POST /tasks`
```json
{
  "title": "Estudar Spring Boot",
  "description": "Focar em injeção de dependência e JPA",
  "status": "IN_PROGRESS"
}
```

**Resposta** `201 Created`
```json
{
  "id": 1,
  "title": "Estudar Spring Boot",
  "description": "Focar em injeção de dependência e JPA",
  "status": "IN_PROGRESS",
  "createdAt": "2026-07-11T12:31:18",
  "updatedAt": "2026-07-11T12:31:18"
}
```

---

## 📊 Status das tarefas

| Status | Descrição |
|---|---|
| `PENDING` | Tarefa criada, aguardando início |
| `IN_PROGRESS` | Tarefa em andamento |
| `COMPLETED` | Tarefa concluída |

---

## 🗄️ Migrations (Flyway)

| Versão | Descrição |
|---|---|
| `V1` | Criação da tabela `tasks` |
| `V2` | Criação da tabela `users` |
| `V3` | Adição de `user_id` em `tasks` (vínculo com usuário) |

---

## 🖥️ Interface Web

Interface web responsiva integrada à API, acessível direto pelo navegador.

**Funcionalidades:**
- Login e cadastro de usuário
- Dashboard com contadores por status
- Criar, editar e deletar tarefas
- Marcar tarefas como concluídas com um clique
- Filtrar por status via sidebar (desktop) ou barra inferior (mobile)
- Dark mode / Light mode persistido no localStorage

**Tecnologias:** HTML5, CSS3 e JavaScript puro — sem frameworks. Servido pelo Spring Boot via `src/main/resources/static/`.

**Estrutura do frontend:**
```
static/
├── index.html     → Estrutura HTML
├── css/
│   └── style.css  → Estilos e design tokens
└── js/
    ├── api.js     → Comunicação com a API (fetch + token)
    ├── auth.js    → Login, registro e logout
    ├── tasks.js   → CRUD de tarefas e renderização
    └── app.js     → Inicialização, tema e controle de telas
```

---

## 🐳 Docker

Build em múltiplos estágios:

1. **Build** — `maven:3.9-eclipse-temurin-21` compila e gera o `.jar`
2. **Runtime** — `eclipse-temurin:21-jre` roda a aplicação em imagem enxuta

---

## 👨‍💻 Autor

**Ícaro Rodrigues Santos**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-icarorod21-blue?style=flat-square&logo=linkedin)](https://www.linkedin.com/in/icarorod21/)
[![GitHub](https://img.shields.io/badge/GitHub-IcaroSantos21-black?style=flat-square&logo=github)](https://github.com/IcaroSantos21)