package br.com.fiap.recall.model;

public class ArquivoPDF extends Arquivo {

    public ArquivoPDF() {
        super();
    }

    public ArquivoPDF(String id, String url, Integer paginasLinhas, String empresaId) {
        super(id, url, paginasLinhas, empresaId);
    }

    @Override
    public String converterParaTexto() {
        return "Texto extraído de arquivo PDF (" + getPaginasLinhas() + " páginas): " + getUrl();
    }

    @Override
    public boolean validarFormato() {
        return getUrl() != null && getUrl().toLowerCase().endsWith(".pdf");
    }

    @Override
    public String getTipoArquivo() {
        return "PDF";
    }
}
