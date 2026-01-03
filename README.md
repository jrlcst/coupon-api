# Coupon API

API para gerenciamento de cupons de desconto, desenvolvida com Spring Boot seguindo princípios de DDD.

## Tecnologias Utilizadas

- Java 25
- Spring Boot 4.0.0
- Spring Data JPA
- H2 Database (em memória)
- Springdoc OpenAPI (Swagger)
- Docker & Docker Compose
- JUnit 5 & MockMvc (Testes)

## Como rodar o projeto

### Automação com Script (Recomendado)

Existe um script que automatiza a execução dos testes e o deploy via Docker:
```bash
chmod +x run-application.sh
./run-application.sh
```
Este script irá rodar os testes unitários e de integração e, caso passem, subirá a API, irá logar os passos,
irá logar as URLs da API e mostrará os logs.

### Localmente com Maven

1. Certifique-se de ter o Java 25 instalado.
2. Execute o comando:
   ```bash
   ./mvnw spring-boot:run
   ```
3. A API estará disponível em `http://localhost:8080/api`.

### Com Docker Compose

1. Execute o comando:
   ```bash
   docker-compose up --build
   ```
2. A API estará disponível em `http://localhost:8080/api`.

## Documentação da API (Swagger)

A documentação interativa da API pode ser acessada em:
`http://localhost:8080/api/swagger-ui.html`

## Rodando os Testes

Para executar os testes de unidade e integração:
```bash
./mvnw clean test
```

## Regras de Negócio Implementadas

- **Criação de Cupom:**
    - Campos obrigatórios: `code`, `description`, `discountValue`, `expirationDate`.
    - Sanitização automática do código (remove caracteres especiais e garante 6 caracteres).
    - Valor de desconto mínimo de 0.5.
    - Data de expiração não pode ser no passado.
- **Exclusão de Cupom:**
    - Soft delete (o registro permanece no banco com status `deleted`).
    - Impedimento de exclusão duplicada.
