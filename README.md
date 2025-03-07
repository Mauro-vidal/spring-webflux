# 📦 Projeto: API de Gerenciamento de Usuários

## 🛠️ Descrição

Este projeto é uma API reativa desenvolvida com Spring WebFlux para gerenciar usuários. A aplicação oferece operações básicas de CRUD (Create, Read, Update, Delete) com validações específicas e tratamento de exceções.

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot 3.x
- Spring WebFlux
- Reactor
- JUnit 5
- Mockito
- WebTestClient

## ⚙️ Funcionalidades

- Criação de usuários
- Consulta de usuário por ID
- Consulta de todos os usuários
- Atualização parcial (PATCH) com validações dinâmicas
- Exclusão de usuários
- Tratamento de exceções com mensagens claras

## 🧪 Testes Implementados

- Teste de sucesso e insucesso para o método `findById`
- Testes de sucesso e insucesso para o método `findAll`
- Teste de sucesso e insucesso para o método `update`
- Teste de sucesso e insucesso para o método `delete`
- Testes unitários e de integração implementados para garantir a confiabilidade da aplicação

## 🔧 Configuração e Execução

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/seu-usuario/nome-do-repositorio.git
   cd nome-do-repositorio
   ```

2. **Configure o ambiente:**

   - Java 17 instalado
   - IDE de sua preferência (IntelliJ, Eclipse, etc.)

3. **Execute a aplicação:**

   ```bash
   ./mvnw spring-boot:run
   ```

## 🔍 Endpoints Disponíveis

### 1. Criar Usuário (POST)

```http
POST /users
```

**Body:**

```json
{
  "name": "João Silva",
  "email": "joao.silva@example.com",
  "password": "senha123"
}
```

**Exemplo de Erro de Validação:**

```json
{
    "timestamp": "17/02/2025 14:22:58",
    "path": "/users",
    "status": 400,
    "error": "Validation Error",
    "message": "Error on validation attributes",
    "errors": [
        {
            "fieldName": "email",
            "message": "field cannot have blank spaces at the beginning or at end"
        },
        {
            "fieldName": "name",
            "message": "field cannot have blank spaces at the beginning or at end"
        },
        {
            "fieldName": "password",
            "message": "field cannot have blank spaces at the beginning or at end"
        },
        {
            "fieldName": "email",
            "message": "invalid email"
        },
        {
            "fieldName": "password",
            "message": "must be between 3 and 50 characters"
        }
    ]
}
```

### 2. Buscar Usuário por ID (GET)

```http
GET /users/{id}
```

### 3. Buscar Todos os Usuários (GET)

```http
GET /users/all
```

### 4. Atualizar Usuário Parcialmente (PATCH)

```http
PATCH /users/{id}
```

**Body (qualquer campo pode ser enviado isoladamente):**

```json
{
  "name": "Novo Nome"
}
```

### 5. Deletar Usuário (DELETE)

```http
DELETE /users/{id}
```

## ⚠️ Tratamento de Erros

Os erros são tratados com mensagens claras, como o seguinte exemplo para `ObjectNotFoundException`:

```json
{
  "timestamp": "2025-02-14T19:55:55",
  "path": "/users/123",
  "status": 404,
  "error": "Not Found",
  "message": "Object not found. Id: 123, Type: User"
}
```

💡 *Desenvolvido com dedicação e código limpo*

