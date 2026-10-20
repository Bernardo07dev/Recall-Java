package br.com.fiap.recall.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ArquivoRequest {

    @NotBlank(message = "A URL do arquivo é obrigatória.")
    @Size(max = 500, message = "A URL deve ter no máximo 500 caracteres.")
    private String url;

    @NotBlank(message = "O tipo do arquivo é obrigatório (PDF, CSV ou XLSX).")
    private String tipo;

    private Integer paginasLinhas;

    public ArquivoRequest() {
    }

    public ArquivoRequest(String url, String tipo, Integer paginasLinhas) {
        this.url = url;
        this.tipo = tipo;
        this.paginasLinhas = paginasLinhas;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getPaginasLinhas() {
        return paginasLinhas;
    }

    public void setPaginasLinhas(Integer paginasLinhas) {
        this.paginasLinhas = paginasLinhas;
    }
}
