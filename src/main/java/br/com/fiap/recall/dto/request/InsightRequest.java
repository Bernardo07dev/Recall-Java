package br.com.fiap.recall.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class InsightRequest {

    @NotBlank(message = "A categoria do insight é obrigatória.")
    private String categoria;

    @NotBlank(message = "A criticidade do insight é obrigatória (BAIXA, MEDIA, ALTA).")
    private String criticidade;

    @NotBlank(message = "O título do insight é obrigatório.")
    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres.")
    private String titulo;

    @Size(max = 1000, message = "O texto do insight deve ter no máximo 1000 caracteres.")
    private String insight;

    @Size(max = 500, message = "A ação sugerida deve ter no máximo 500 caracteres.")
    private String acaoSugerida;

    public InsightRequest() {
    }

    public InsightRequest(String categoria, String criticidade, String titulo, String insight, String acaoSugerida) {
        this.categoria = categoria;
        this.criticidade = criticidade;
        this.titulo = titulo;
        this.insight = insight;
        this.acaoSugerida = acaoSugerida;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCriticidade() {
        return criticidade;
    }

    public void setCriticidade(String criticidade) {
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
}
