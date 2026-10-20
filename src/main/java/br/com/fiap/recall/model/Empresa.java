package br.com.fiap.recall.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Empresa {

    private String id;
    private String nome;
    private String industry;
    private String summarization;
    private String jsonIntel;
    private LocalDateTime dataCriacao;
    private List<Arquivo> arquivos;
    private List<Insight> insights;

    public Empresa() {
        this.arquivos = new ArrayList<>();
        this.insights = new ArrayList<>();
    }

    public Empresa(String id, String nome, String industry) {
        this.id = id;
        this.nome = nome;
        this.industry = industry;
        this.dataCriacao = LocalDateTime.now();
        this.arquivos = new ArrayList<>();
        this.insights = new ArrayList<>();
    }

    public Empresa(String id, String nome, String industry, String summarization, String jsonIntel) {
        this.id = id;
        this.nome = nome;
        this.industry = industry;
        this.summarization = summarization;
        this.jsonIntel = jsonIntel;
        this.dataCriacao = LocalDateTime.now();
        this.arquivos = new ArrayList<>();
        this.insights = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getSummarization() {
        return summarization;
    }

    public void setSummarization(String summarization) {
        this.summarization = summarization;
    }

    public String getJsonIntel() {
        return jsonIntel;
    }

    public void setJsonIntel(String jsonIntel) {
        this.jsonIntel = jsonIntel;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public List<Arquivo> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<Arquivo> arquivos) {
        this.arquivos = arquivos;
    }

    public List<Insight> getInsights() {
        return insights;
    }

    public void setInsights(List<Insight> insights) {
        this.insights = insights;
    }
}
