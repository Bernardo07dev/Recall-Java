package br.com.fiap.recall.model;

import java.time.LocalDateTime;

public abstract class Arquivo {

    private String id;
    private String url;
    private Integer paginasLinhas;
    private String empresaId;
    private LocalDateTime dataUpload;

    public Arquivo() {
    }

    public Arquivo(String id, String url, Integer paginasLinhas, String empresaId) {
        this.id = id;
        this.url = url;
        this.paginasLinhas = paginasLinhas;
        this.empresaId = empresaId;
        this.dataUpload = LocalDateTime.now();
    }

    public abstract String converterParaTexto();

    public abstract boolean validarFormato();

    public abstract String getTipoArquivo();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPaginasLinhas() {
        return paginasLinhas;
    }

    public void setPaginasLinhas(Integer paginasLinhas) {
        this.paginasLinhas = paginasLinhas;
    }

    public String getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(String empresaId) {
        this.empresaId = empresaId;
    }

    public LocalDateTime getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(LocalDateTime dataUpload) {
        this.dataUpload = dataUpload;
    }
}
