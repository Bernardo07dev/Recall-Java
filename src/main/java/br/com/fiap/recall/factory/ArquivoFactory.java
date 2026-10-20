package br.com.fiap.recall.factory;

import br.com.fiap.recall.exception.RegraNegocioException;
import br.com.fiap.recall.model.Arquivo;
import br.com.fiap.recall.model.ArquivoCSV;
import br.com.fiap.recall.model.ArquivoPDF;
import br.com.fiap.recall.model.ArquivoXLSX;

public class ArquivoFactory {

    public static Arquivo criar(String tipo, String id, String url, Integer paginasLinhas, String empresaId) {
        if (tipo == null || tipo.isBlank()) {
            throw new RegraNegocioException("Tipo de arquivo é obrigatório.");
        }

        switch (tipo.trim().toUpperCase()) {
            case "PDF":
                return new ArquivoPDF(id, url, paginasLinhas, empresaId);
            case "CSV":
                return new ArquivoCSV(id, url, paginasLinhas, empresaId);
            case "XLSX":
            case "XLS":
                return new ArquivoXLSX(id, url, paginasLinhas, empresaId);
            default:
                throw new RegraNegocioException("Tipo de arquivo inválido: " + tipo + ". Utilize PDF, CSV ou XLSX.");
        }
    }
}
