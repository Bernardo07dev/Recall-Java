# 🏢 Recall Java

Sistema modular em Java voltado para a gestão de empresas, processamento de documentos heterogéneos (PDF, CSV, XLSX) e geração automatizada de insights inteligentes com integração a ferramentas de mercado (Jira). O sistema conta com uma interface operacional síncrona baseada em diálogos visuais com `JOptionPane` (Java Swing).

---

## 📌 Arquitetura do Sistema

O projeto adota o padrão **Package by Feature** (Pacote por Funcionalidade), o que garante alta coesão, isolamento de escopo e facilidade para escalar o ecossistema.

```text
src/main/java/com/sistema/
├── Main.java                 # Ponto de entrada do sistema e loop da interface
├── usuario/                  # Gestão de acessos e regras de herança polimórfica
│   ├── Usuario.java          # Classe Abstrata Pai
│   ├── Admin.java
│   ├── Manager.java
│   └── Viewer.java
├── arquivo/                  # Módulo de extração e conversão de dados brutas
│   ├── Arquivo.java          # Classe Abstrata Pai
│   ├── ArquivoPDF.java
│   ├── ArquivoCSV.java
│   └── ArquivoXLSX.java
├── empresa/                  # Entidade Core agregadora
│   └── Empresa.java
└── insight/                  # Módulo de inteligência analítica
    ├── Insight.java
    ├── Categoria.java        # Enum (FINANCEIRO, OPERACIONAL, etc.)
    └── Criticidade.java      # Enum (BAIXA, MEDIA, ALTA)