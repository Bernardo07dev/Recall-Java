package br.com.fiap.recall.model;

public class ArquivoCSV extends Arquivo {

    public ArquivoCSV() {
        super();
    }

    public ArquivoCSV(String id, String url, Integer paginasLinhas, String empresaId) {
        super(id, url, paginasLinhas, empresaId);
    }

    @Override
    public String converterParaTexto() {
        return "Linhas tabulares processadas de arquivo CSV (" + getPaginasLinhas() + " linhas): " + getUrl();
    }

    @Override
    public boolean validarFormato() {
        return getUrl() != null && getUrl().toLowerCase().endsWith(".csv");
    }

    @Override
    public String getTipoArquivo() {
        return "CSV";
    }
}
