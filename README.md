# 🏢 Recall Java — Inteligência Conversacional

API RESTful desenvolvida em Java e Spring Boot para o **FIAP x TOTVS Challenge** (Sprint 4 — Domain Driven Design).

A plataforma processa documentos e transcrições de reuniões (PDF, CSV, XLSX), extrai inteligência de vendas e Customer Success (risco de churn, oportunidades de upsell, ameaças de concorrentes), estrutura contextos corporativos por empresa e gera tarefas acionáveis integradas ao Jira.

---

## 👥 Integrantes da Equipe Recall

- **Bernardo Moreira** — RM 564103
- **Pedro Batista** — RM 563220
- **Renan Jordão** — RM 560618
- **Larissa Shiba** — RM 560462

---

## 🛠️ Stack Técnica

- **Java 21**
- **Spring Boot 3.3.4** (Maven Wrapper)
- **Spring Web** (Controllers RESTful)
- **Spring JDBC (`JdbcTemplate`)**: Persistência relacional com SQL explícito (sem ORM/JPA para visibilidade da camada DAO)
- **Oracle Database (`ojdbc11`)**: Conexão com Oracle Cloud FIAP
- **Bean Validation (`jakarta.validation`)**: Validação declarativa de entrada nos DTOs
- **springdoc-openapi (v2.6.0)**: Documentação interativa via Swagger UI

---

## 🏛️ Arquitetura em Camadas & Padrões de Projeto

O projeto segue arquitetura em camadas orientada ao domínio:

```text
src/main/java/br/com/fiap/recall/
├── RecallApplication.java
├── config/             # CORS e OpenAPI/Swagger
├── controller/         # Endpoints REST e status codes HTTP
├── service/            # Regras de negócio e validações
├── dao/                # Interfaces de acesso a dados e JDBC
│   └── impl/           # Implementação com JdbcTemplate e RowMapper
├── dto/
│   ├── request/        # Entrada validada com Bean Validation
│   └── response/       # Saída desacoplada do modelo (Records)
├── factory/            # Criação polimórfica (UsuarioFactory, ArquivoFactory)
├── model/              # Entidades centrais do domínio e regras de negócio
└── exception/          # Tratamento centralizado de erros (@RestControllerAdvice)
```

### Padrões Aplicados

- **DAO (Data Access Object)**: Desacoplamento entre lógica de negócio e persistência SQL com `JdbcTemplate`.
- **DTO (Data Transfer Object)**: Entrada (`*Request`) com validação de formato e saída (`*Response`) com `records`.
- **Factory**: `UsuarioFactory` e `ArquivoFactory` instanciam dinamicamente subclasses polimórficas a partir do discriminador persistido no banco.
- **Service**: Centraliza validações de domínio, integridade relacional e geração de tarefas Jira.
- **Global Exception Handler**: `@RestControllerAdvice` padronizando respostas de erro HTTP (400, 404, 500) com timestamp, status e path.

---

## 📋 Endpoints da API REST

Prefixo base: `/api`

### Empresas
| Verbo | Rota | Descrição | Status Sucesso |
|---|---|---|---|
| `GET` | `/api/empresas` | Lista todas as empresas | `200 OK` |
| `GET` | `/api/empresas/{id}` | Busca empresa detalhada com arquivos e insights | `200 OK` |
| `POST` | `/api/empresas` | Cadastra nova empresa | `201 Created` |
| `PUT` | `/api/empresas/{id}` | Atualiza dados cadastrais | `200 OK` |
| `DELETE` | `/api/empresas/{id}` | Remove empresa e dependentes em cascata | `204 No Content` |

### Insights
| Verbo | Rota | Descrição | Status Sucesso |
|---|---|---|---|
| `GET` | `/api/empresas/{empresaId}/insights` | Lista insights da empresa | `200 OK` |
| `GET` | `/api/insights/{id}` | Busca insight por ID | `200 OK` |
| `POST` | `/api/empresas/{empresaId}/insights` | Cadastra insight e gera task Jira | `201 Created` |
| `PUT` | `/api/insights/{id}` | Atualiza categoria, criticidade e textos | `200 OK` |
| `DELETE` | `/api/insights/{id}` | Remove insight | `204 No Content` |

### Arquivos
| Verbo | Rota | Descrição | Status Sucesso |
|---|---|---|---|
| `GET` | `/api/empresas/{empresaId}/arquivos` | Lista arquivos vinculados à empresa | `200 OK` |
| `GET` | `/api/arquivos/{id}` | Busca arquivo por ID | `200 OK` |
| `POST` | `/api/empresas/{empresaId}/arquivos` | Cadastra arquivo (PDF, CSV, XLSX) | `201 Created` |
| `PUT` | `/api/arquivos/{id}` | Atualiza URL e formato | `200 OK` |
| `DELETE` | `/api/arquivos/{id}` | Remove arquivo | `204 No Content` |

### Usuários
| Verbo | Rota | Descrição | Status Sucesso |
|---|---|---|---|
| `GET` | `/api/usuarios` | Lista todos os usuários | `200 OK` |
| `GET` | `/api/usuarios/{id}` | Busca usuário por ID | `200 OK` |
| `POST` | `/api/usuarios` | Cadastra usuário (`ADMIN`, `MANAGER`, `VIEWER`) | `201 Created` |
| `PUT` | `/api/usuarios/{id}` | Atualiza dados do usuário | `200 OK` |
| `DELETE` | `/api/usuarios/{id}` | Remove usuário | `204 No Content` |

---

## 🚀 Como Executar

### Pré-requisitos
- JDK 21+ instalado e configurado no `PATH`
- Conexão de rede com acesso ao Oracle FIAP

### Execução via Maven Wrapper

```bash
# Windows (PowerShell / CMD)
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

A aplicação subirá por padrão na porta `8080`.

### Documentação Interativa (Swagger)
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

---

## 🗄️ Banco de Dados

O script DDL com as tabelas `TB_RECALL_USUARIO`, `TB_RECALL_EMPRESA`, `TB_RECALL_ARQUIVO` e `TB_RECALL_INSIGHT` está localizado em:
- [src/main/resources/schema.sql](file:///c:/Users/berna/Documents/Projects/Recall-Java/src/main/resources/schema.sql)
- [schema.sql](file:///c:/Users/berna/Documents/Projects/Recall-Java/schema.sql) (com rotinas de `DROP CASCADE`)
