package br.com.fiap.recall.model;

import java.time.LocalDateTime;

public class Insight {

    private String id;
    private Categoria categoria;
    private Criticidade criticidade;
    private String titulo;
    private String insight;
    private String acaoSugerida;
    private String empresaId;
    private LocalDateTime dataCriacao;

    public Insight() {
    }

    public Insight(String id, Categoria categoria, Criticidade criticidade, String titulo, String insight, String acaoSugerida, String empresaId) {
        this.id = id;
        this.categoria = categoria;
        this.criticidade = criticidade;
        this.titulo = titulo;
        this.insight = insight;
        this.acaoSugerida = acaoSugerida;
        this.empresaId = empresaId;
        this.dataCriacao = LocalDateTime.now();
    }

    public String gerarTaskJira() {
        return String.format("[JIRA-%s] [%s] %s | Ação: %s",
                criticidade != null ? criticidade.name() : "INFO",
                categoria != null ? categoria.name() : "GERAL",
                titulo != null ? titulo : "Sem título",
                acaoSugerida != null ? acaoSugerida : "Nenhuma ação definida");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Criticidade getCriticidade() {
        return criticidade;
    }

    public void setCriticidade(Criticidade criticidade) {
        this.criticidade = criticidade;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getInsight() {
        return insight;
    }

    public void setInsight(String insight) {
        this.insight = insight;
    }

    public String getAcaoSugerida() {
        return acaoSugerida;
    }

    public void setAcaoSugerida(String acaoSugerida) {
        this.acaoSugerida = acaoSugerida;
    }

    public String getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(String empresaId) {
        this.empresaId = empresaId;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
