package br.com.fiap.recall.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmpresaRequest {

    @NotBlank(message = "O nome da empresa é obrigatório.")
    @Size(max = 120, message = "O nome da empresa deve ter no máximo 120 caracteres.")
    private String nome;

    @NotBlank(message = "O setor de atuação (industry) é obrigatório.")
    @Size(max = 80, message = "O setor de atuação deve ter no máximo 80 caracteres.")
    private String industry;

    private String summarization;
    private String jsonIntel;

    public EmpresaRequest() {
    }

    public EmpresaRequest(String nome, String industry, String summarization, String jsonIntel) {
        this.nome = nome;
        this.industry = industry;
        this.summarization = summarization;
        this.jsonIntel = jsonIntel;
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
}
