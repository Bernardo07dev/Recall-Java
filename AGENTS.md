# Recall Java — Diretrizes do Projeto & Guia de Desenvolvimento (AGENTS.md)

Este documento centraliza as diretrizes arquiteturais, regras de negócio, convenções de código e instruções operacionais para o desenvolvimento do **Recall-Java** (Sprint 4 — Domain Driven Design).

---

## 1. Visão Geral e Contexto de Negócio

- **Solução**: Recall Java DDD
- **Equipe**: Recall
- **Disciplina**: Domain Driven Design — Java (FIAP)
- **Desafio**: FIAP x TOTVS Challenge — Inteligência Conversacional
- **Proposta de Valor**: Plataforma alimentada por IA que processa arquivos e transcrições de reuniões (PDF, CSV, XLSX), extrai inteligência de vendas e CS (risco de churn, upsell, concorrentes), estrutura o contexto por empresa e gera tarefas acionáveis (Jira).
- **Integrantes da Equipe**:
  - Bernardo Moreira (RM 564103)
  - Pedro Batista (RM 563220)
  - Renan Jordão (RM 560618)
  - Larissa Shiba (RM 560462)
- **Origem**: Sprint 3 era um sistema em memória com `JOptionPane`. O Sprint 4 mantém o mesmo domínio e transforma em API REST com persistência (ver seção 13).

---

## 2. Requisitos da Entrega (Sprint 4)

Entrega: projeto Java compactado (`.zip`) + documentação (`.pdf`) via Microsoft Teams.

| Componente | Pontuação | Descrição |
|---|---|---|
| **Camada Model** | 10 pts | Todas as classes necessárias, de acordo com o banco de dados, preservando o domínio e o polimorfismo. |
| **Camada DAO e Service** | 20 pts | Todas as funcionalidades que o Front-End precisa. O CRUD deve funcionar totalmente. Service com validações. |
| **API RESTful** | 30 pts | Todos os endpoints necessários para o Front-End (GET, POST, PUT, DELETE), JSON de entrada/saída e status codes corretos (200, 201, 204, 400, 404, 500). |
| **Boas Práticas & Padrões** | 20 pts | Regras de nomenclatura, tratamento de exceções, padrões de projeto (DAO, DTO, Service, Factory, Singleton). |
| **Documentação (PDF)** | 20 pts | Checklist completo na seção 11. |

---

## 3. Stack Técnica

- **Java 25** (mesma versão do `SpringProject` do autor)
- **Spring Boot** (mesma versão do `SpringProject`), **Maven**
- **Spring Web**: controllers REST
- **Spring JDBC (`JdbcTemplate`)**: camada DAO com SQL explícito (sem JPA, para o DAO ficar visível e avaliável)
- **Bean Validation** (`spring-boot-starter-validation`): validação dos DTOs de entrada
- **springdoc-openapi**: Swagger UI
- **Oracle** (`ojdbc17`), alinhado ao DDL da seção 6. Credenciais por variável de ambiente em `application.properties`, nunca commitadas.
- Sem Lombok.

---

## 4. Padrão Arquitetural em Camadas

```text
src/main/java/br/com/fiap/recall/
├── RecallApplication.java
├── config/             # CORS e OpenAPI/Swagger
├── model/              # Entidades do domínio
│   ├── Empresa.java
│   ├── Usuario.java    # Classe abstrata (Admin, Manager, Viewer)
│   ├── Admin.java
│   ├── Manager.java
│   ├── Viewer.java
│   ├── Arquivo.java    # Classe abstrata (ArquivoPDF, ArquivoCSV, ArquivoXLSX)
│   ├── ArquivoPDF.java
│   ├── ArquivoCSV.java
│   ├── ArquivoXLSX.java
│   ├── Insight.java
│   ├── Categoria.java  # Enum (CHURN_RISK, UPSELL, COMPETITOR_THREAT, etc.)
│   └── Criticidade.java# Enum (BAIXA, MEDIA, ALTA)
├── factory/            # Cria a subclasse certa a partir do tipo (ADMIN/MANAGER/VIEWER, PDF/CSV/XLSX)
│   ├── UsuarioFactory.java
│   └── ArquivoFactory.java
├── dto/                # Data Transfer Objects
│   ├── request/        # Entrada com validações (@NotBlank, @Email, etc.)
│   └── response/       # Saída desacoplada da entidade
├── dao/                # Acesso ao banco (JdbcTemplate + SQL)
│   ├── EmpresaDao.java
│   ├── UsuarioDao.java
│   ├── ArquivoDao.java
│   ├── InsightDao.java
│   └── impl/
├── service/            # Regras de negócio e validações
│   ├── EmpresaService.java
│   ├── UsuarioService.java
│   ├── ArquivoService.java
│   └── InsightService.java
├── controller/         # Endpoints HTTP e status codes
│   ├── EmpresaController.java
│   ├── UsuarioController.java
│   ├── ArquivoController.java
│   └── InsightController.java
└── exception/          # Tratamento centralizado de erros
    ├── EntidadeNaoEncontradaException.java
    ├── RegraNegocioException.java
    ├── ErroResponse.java
    └── GlobalExceptionHandler.java
```

Fluxo: `Controller` (DTO) → `Service` (validação/regra) → `Dao` (SQL) → Oracle. O controller nunca acessa o DAO e o DAO nunca conhece DTO.

---

## 5. Regras de Domínio e Polimorfismo

1. **Usuários e Controle de Acesso**:
   - `Usuario` é abstrato. Possui `id`, `nome`, `email`, `cargo`, `area`.
   - `podeCriarContexto()`:
     - `Admin`: `true` (acesso irrestrito).
     - `Manager`: `true` (gerencia equipe e empresas associadas).
     - `Viewer`: `false` (somente leitura).
   - O tipo é persistido em `TP_USUARIO`. Os atributos extras do diagrama do Sprint 3 (`nivelAcesso`, `equipe`, `empresasVisiveis`) **não existem no banco e saem do model**. O polimorfismo fica no comportamento.

2. **Arquivos e Extração de Texto**:
   - `Arquivo` é abstrato. Possui `id`, `url`, `tipo`, `empresaId`.
   - `converterParaTexto()` (abstrato):
     - `ArquivoPDF`: extrai texto das páginas.
     - `ArquivoCSV`: faz parse das linhas tabulares.
     - `ArquivoXLSX`: extrai planilhas e métricas.
   - `validarFormato()`: URL não vazia e extensão compatível com a subclasse.
   - O tipo é persistido em `TP_ARQUIVO`; páginas/linhas em `NR_PAGINAS_LINHAS`.

3. **Empresa**:
   - Agregador central. Possui `id`, `nome`, `industry`, `summarization`, `jsonIntel`, além de `Arquivo` e `Insight`.
   - `nome` e `industry` obrigatórios. Não pode existir duas empresas com o mesmo `nome`.

4. **Insight**:
   - Possui `categoria`, `criticidade`, `titulo`, `descricao`, `acaoSugerida`, `empresaId`.
   - `gerarTaskJira()`: monta o texto da task para Jira/CRM.

5. **Validações no Service**:
   - E-mail de usuário único e com formato válido.
   - Empresa deve existir ao criar insight ou arquivo (senão `404`).
   - Arquivo com extensão incompatível com o tipo é rejeitado (`400`).
   - Tipos, categorias e criticidades inválidos são rejeitados (`400`).

---

## 6. Modelo Relacional do Banco de Dados (DDL)

```sql
-- TB_RECALL_USUARIO
CREATE TABLE TB_RECALL_USUARIO (
    ID_USUARIO   VARCHAR2(36) PRIMARY KEY,
    NM_USUARIO   VARCHAR2(100) NOT NULL,
    DS_EMAIL     VARCHAR2(120) NOT NULL UNIQUE,
    DS_CARGO     VARCHAR2(50) NOT NULL,
    DS_AREA      VARCHAR2(50) NOT NULL,
    TP_USUARIO   VARCHAR2(20) NOT NULL,
    DT_CRIACAO   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- TB_RECALL_EMPRESA
CREATE TABLE TB_RECALL_EMPRESA (
    ID_EMPRESA        VARCHAR2(36) PRIMARY KEY,
    NM_EMPRESA        VARCHAR2(120) NOT NULL,
    DS_INDUSTRY       VARCHAR2(80) NOT NULL,
    DS_SUMMARIZATION  CLOB,
    DS_JSON_INTEL     CLOB,
    DT_CRIACAO        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- TB_RECALL_ARQUIVO
CREATE TABLE TB_RECALL_ARQUIVO (
    ID_ARQUIVO         VARCHAR2(36) PRIMARY KEY,
    DS_URL             VARCHAR2(500) NOT NULL,
    TP_ARQUIVO         VARCHAR2(20) NOT NULL,
    NR_PAGINAS_LINHAS  NUMBER,
    ID_EMPRESA         VARCHAR2(36) NOT NULL,
    DT_UPLOAD          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT FK_ARQUIVO_EMPRESA FOREIGN KEY (ID_EMPRESA)
        REFERENCES TB_RECALL_EMPRESA(ID_EMPRESA) ON DELETE CASCADE
);

-- TB_RECALL_INSIGHT
CREATE TABLE TB_RECALL_INSIGHT (
    ID_INSIGHT        VARCHAR2(36) PRIMARY KEY,
    DS_CATEGORIA      VARCHAR2(40) NOT NULL,
    DS_CRITICIDADE    VARCHAR2(20) NOT NULL,
    NM_TITULO         VARCHAR2(150) NOT NULL,
    DS_INSIGHT        VARCHAR2(1000),
    DS_ACAO_SUGERIDA  VARCHAR2(500),
    ID_EMPRESA        VARCHAR2(36) NOT NULL,
    DT_CRIACAO        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT FK_INSIGHT_EMPRESA FOREIGN KEY (ID_EMPRESA)
        REFERENCES TB_RECALL_EMPRESA(ID_EMPRESA) ON DELETE CASCADE
);
```

O script fica em `src/main/resources/schema.sql` e deve ser a fonte do DER entregue no PDF.

### 6.1. Dados de Conexão (Oracle FIAP)

| Propriedade | Valor |
|---|---|
| **Host** | `oracle.fiap.com.br` |
| **Porta** | `1521` |
| **Service Name / SID** | `ORCL` |
| **JDBC URL** | `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL` |
| **Username** | `RM560618` |
| **Password** | `251100` |

Exemplo de configuração local (`application.properties` ou variáveis de ambiente):
```properties
spring.datasource.url=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
spring.datasource.username=RM560618
spring.datasource.password=251100
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
```

---

## 7. Especificação da API RESTful

CRUD completo para as quatro entidades. Prefixo `/api`.

### 7.1. Empresas
| Verbo | URI | Sucesso | Observação |
|---|---|---|---|
| GET | `/api/empresas` | `200` | Lista |
| GET | `/api/empresas/{id}` | `200` | Detalhe com insights e arquivos |
| POST | `/api/empresas` | `201` | Valida `nome` e `industry` |
| PUT | `/api/empresas/{id}` | `200` | Atualiza |
| DELETE | `/api/empresas/{id}` | `204` | Exclui filhos em cascata |

### 7.2. Insights
| Verbo | URI | Sucesso | Observação |
|---|---|---|---|
| GET | `/api/empresas/{empresaId}/insights` | `200` | Lista da empresa |
| GET | `/api/insights/{id}` | `200` | Busca por ID |
| POST | `/api/empresas/{empresaId}/insights` | `201` | Cria e devolve a task Jira |
| PUT | `/api/insights/{id}` | `200` | Atualiza dados/criticidade |
| DELETE | `/api/insights/{id}` | `204` | Remove |

### 7.3. Arquivos
| Verbo | URI | Sucesso | Observação |
|---|---|---|---|
| GET | `/api/empresas/{empresaId}/arquivos` | `200` | Lista da empresa |
| GET | `/api/arquivos/{id}` | `200` | Busca por ID |
| POST | `/api/empresas/{empresaId}/arquivos` | `201` | Cadastra e converte |
| PUT | `/api/arquivos/{id}` | `200` | Atualiza URL/tipo |
| DELETE | `/api/arquivos/{id}` | `204` | Remove |

### 7.4. Usuários
| Verbo | URI | Sucesso | Observação |
|---|---|---|---|
| GET | `/api/usuarios` | `200` | Lista |
| GET | `/api/usuarios/{id}` | `200` | Busca por ID |
| POST | `/api/usuarios` | `201` | Admin, Manager ou Viewer |
| PUT | `/api/usuarios/{id}` | `200` | Atualiza |
| DELETE | `/api/usuarios/{id}` | `204` | Remove |

Erros possíveis em qualquer endpoint: `400`, `404`, `500`. Esta tabela vai direto para o PDF (URI, verbo, status).

---

## 8. Padrão de Resposta de Erro

Erros 400, 404 e 500 retornam:

```json
{
  "timestamp": "2026-10-19T15:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Empresa com id 'e4a2...' não encontrada.",
  "path": "/api/empresas/e4a2..."
}
```

Mapeamento no `GlobalExceptionHandler`:
- `EntidadeNaoEncontradaException` → `404`
- `RegraNegocioException` e `MethodArgumentNotValidException` → `400`
- `Exception` genérica → `500` (sem expor stack trace)

---

## 9. Estilo de Código (obrigatório)

O código deve parecer escrito por um **estudante de segundo ano de Engenharia de Software**: simples, direto e legível.

- Classes comuns com atributos privados, construtor e getters/setters escritos à mão. Sem Lombok.
- Records são aceitos apenas para DTOs.
- Métodos curtos, com `if/else` e `for` simples. Evitar streams encadeadas, generics avançados, reflection e abstrações genéricas.
- Nomes em português e coerentes com o domínio (`buscarPorId`, `listarTodos`, `inserir`, `atualizar`, `excluir`). Classes em `PascalCase`, métodos e variáveis em `camelCase`, constantes em `UPPER_SNAKE_CASE`.
- **Sem comentários óbvios ou decorativos.** Nada de `// construtor`, `// getters`, `// retorna o id`. Comentário só quando o motivo de algo não for evidente pelo código. Em geral, nenhum.
- Sem código morto, `System.out.println` de depuração ou TODO esquecido.
- Injeção de dependência por construtor.
- Um arquivo por classe, sem classes utilitárias "faz-tudo".

---

## 10. Padrões de Projeto

| Padrão | Onde |
|---|---|
| **DAO** | `dao/` com interface + `impl/` usando `JdbcTemplate` |
| **DTO** | `dto/request` e `dto/response`; controller nunca expõe o model |
| **Service** | `service/` concentra validações e regras |
| **Factory** | `UsuarioFactory` e `ArquivoFactory` instanciam a subclasse pelo tipo, usados no DAO ao ler do banco e no Service ao criar |
| **Singleton** | Beans do Spring (`@Service`, `@Repository`, `@RestController`) são singletons por padrão |
| **Exception Handler centralizado** | `@RestControllerAdvice` |

---

## 11. Checklist da Documentação PDF (20 pts)

- [ ] Capa: nomes e RMs dos integrantes, **nome da equipe**, nome da solução (Recall)
- [ ] Sumário
- [ ] Objetivo e escopo do projeto, descrito de forma objetiva
- [ ] Breve descrição das principais funcionalidades
- [ ] Tabela de endpoints (URI, verbo HTTP, status de resposta)
- [ ] Protótipo: prints das telas **implementadas** (Front-End do Recall consumindo a API)
- [ ] Modelo do banco de dados (DER + DDL da seção 6)
- [ ] Diagrama de classes **atualizado** (o `Diagrama-RJ.jpeg` do Sprint 3 está defasado: não tem enums, factory, DAO, Service e Controller, e traz atributos removidos do model)

---

## 12. Comandos de Build, Execução e Testes

Maven não está instalado na máquina. Usar o **Maven Wrapper** gerado pelo Spring Initializr.

- **Compilar e rodar testes**:
  ```bash
  ./mvnw clean test
  ```
- **Executar a API localmente**:
  ```bash
  ./mvnw spring-boot:run
  ```
- **Swagger UI**:
  ```text
  http://localhost:8080/swagger-ui.html
  ```
- **Gerar pacote para entrega**:
  ```bash
  ./mvnw clean package
  ```
  Para o `.zip`, compactar o projeto sem `target/`, `.idea/` e `out/`.

---

## 13. Migração Sprint 3 → Sprint 4

| Antes (Sprint 3) | Depois (Sprint 4) |
|---|---|
| Pacotes `empresa`, `usuario`, `arquivo`, `insight` na raiz | `br.com.fiap.recall.model` em estrutura Maven (`src/main/java`) |
| `Main.java` com `JOptionPane` | Removido. Entrada é a API REST |
| `HashMap`/`ArrayList` estáticos | Tabelas `TB_RECALL_*` via DAO |
| Regras no loop da UI | Camada Service |
| `podecriarcontexto()` | `podeCriarContexto()` (nomenclatura) |
| `Boolean` wrapper nos retornos | `boolean` primitivo |
| `Insight` com `gerarTaskJira()` que retorna só texto | Mantido, usado na resposta do `POST` de insight |
| Atributos `nivelAcesso`, `equipe`, `empresasVisiveis` | Removidos (sem coluna no banco) |
