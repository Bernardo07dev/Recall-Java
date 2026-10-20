package br.com.fiap.recall.model;

public class ArquivoXLSX extends Arquivo {

    public ArquivoXLSX() {
        super();
    }

    public ArquivoXLSX(String id, String url, Integer paginasLinhas, String empresaId) {
        super(id, url, paginasLinhas, empresaId);
    }

    @Override
    public String converterParaTexto() {
        return "Planilhas e métricas extraídas de arquivo XLSX (" + getPaginasLinhas() + " abas/linhas): " + getUrl();
    }

    @Override
    public boolean validarFormato() {
        return getUrl() != null && (getUrl().toLowerCase().endsWith(".xlsx") || getUrl().toLowerCase().endsWith(".xls"));
    }

    @Override
    public String getTipoArquivo() {
        return "XLSX";
    }
}
