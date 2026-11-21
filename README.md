# AppWeb — versão refatorada

API REST Spring Boot 3.4.2 + Java 21 + PostgreSQL + Spring Security + JWT.

## Melhorias realizadas

- Remoção de DTOs duplicados/legados.
- Padronização de pacotes e nomes.
- JWT com `Instant` e validação do prefixo `Bearer`.
- Credenciais e segredo JWT por variáveis de ambiente.
- `ddl-auto=update` por padrão, em vez de `create-drop`.
- Roles normalizadas (`ROLE_USER`, `ROLE_ADMIN`, `ROLE_PASTOR`).
- Exceções centralizadas.
- Validações uniformes.
- Endereço incluído no DTO de membro.
- Consultas de totais executadas no banco com `SUM`.
- `BigDecimal` mantido para valores monetários.
- PDF padronizado com OpenPDF.
- Remoção de exposição de entidades JPA nos endpoints.
- Respostas HTTP consistentes.

## Configuração

1. Copie `.env.example` para `.env` ou exporte as variáveis no shell.
2. Configure `DB_PASSWORD`, `JWT_SECRET` e `ADMIN_PASSWORD`.
3. Confirme que o PostgreSQL está ativo.
4. Execute:

```bash
mvn clean verify
mvn spring-boot:run
```

## Primeiro login

O `DataLoader` cria o administrador definido por:

```text
ADMIN_USERNAME
ADMIN_PASSWORD
```

A senha é armazenada usando BCrypt.

## Endpoints principais

### Autenticação

```text
POST /auth/register
POST /auth/login
```

### Usuários — ADMIN

```text
GET    /users
GET    /users/{id}
PUT    /users/{id}
PATCH  /users/{id}/roles
DELETE /users/{id}
```

### Membros

```text
GET    /membros
GET    /membros/{id}
GET    /membros/buscar?nome=Joao
GET    /membros/idade?idadeMinima=18&idadeMaxima=60

POST   /membros       ADMIN
PUT    /membros/{id}  ADMIN
DELETE /membros/{id}  ADMIN
```

### Financeiro

```text
GET    /financeiros/{id}
GET    /financeiros/consulta/membro/{id}
GET    /financeiros/consulta/membro?nome=Joao
GET    /financeiros/consulta/periodo?dataInicio=2026-01-01&dataFim=2026-12-31
GET    /financeiros/consulta/entradas?dataInicio=...&dataFim=...
GET    /financeiros/consulta/saidas?dataInicio=...&dataFim=...

POST   /financeiros       ADMIN
PUT    /financeiros/{id}  ADMIN
DELETE /financeiros/{id}  ADMIN
```

### Relatórios

```text
GET /financeiros/relatorios/saldo?dataInicio=...&dataFim=...
GET /financeiros/relatorios/entradas?dataInicio=...&dataFim=...
GET /financeiros/relatorios/saidas?dataInicio=...&dataFim=...
GET /financeiros/relatorios/resumo?dataInicio=...&dataFim=...
```

### PDF

```text
GET /financeiros/pdf/financeiro?dataInicio=...&dataFim=...
GET /financeiros/pdf/entradas?dataInicio=...&dataFim=...
GET /financeiros/pdf/saidas?dataInicio=...&dataFim=...
```

## Observação sobre banco existente

Se o banco atual foi criado pelo projeto anterior, faça backup antes de executar a versão refatorada.

O código não usa mais `create-drop`, portanto a intenção é preservar os dados existentes.
