package br.com.fiap.recall.service;

import br.com.fiap.recall.dao.ArquivoDao;
import br.com.fiap.recall.dao.EmpresaDao;
import br.com.fiap.recall.dto.request.ArquivoRequest;
import br.com.fiap.recall.dto.response.ArquivoResponse;
import br.com.fiap.recall.exception.EntidadeNaoEncontradaException;
import br.com.fiap.recall.exception.RegraNegocioException;
import br.com.fiap.recall.factory.ArquivoFactory;
import br.com.fiap.recall.model.Arquivo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ArquivoService {

    private final ArquivoDao arquivoDao;
    private final EmpresaDao empresaDao;

    public ArquivoService(ArquivoDao arquivoDao, EmpresaDao empresaDao) {
        this.arquivoDao = arquivoDao;
        this.empresaDao = empresaDao;
    }

    public List<ArquivoResponse> listarPorEmpresa(String empresaId) {
        if (!empresaDao.existePorId(empresaId)) {
            throw new EntidadeNaoEncontradaException("Empresa com ID '" + empresaId + "' não encontrada.");
        }

        List<Arquivo> arquivos = arquivoDao.listarPorEmpresa(empresaId);
        List<ArquivoResponse> responses = new ArrayList<>();
        for (Arquivo a : arquivos) {
            responses.add(paraResponse(a));
        }
        return responses;
    }

    public ArquivoResponse buscarPorId(String id) {
        Arquivo arquivo = arquivoDao.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Arquivo com ID '" + id + "' não encontrado."));
        return paraResponse(arquivo);
    }

    public ArquivoResponse cadastrar(String empresaId, ArquivoRequest request) {
        if (!empresaDao.existePorId(empresaId)) {
            throw new EntidadeNaoEncontradaException("Empresa com ID '" + empresaId + "' não encontrada.");
        }

        String id = UUID.randomUUID().toString();
        Arquivo arquivo = ArquivoFactory.criar(
                request.getTipo(),
                id,
                request.getUrl().trim(),
                request.getPaginasLinhas(),
                empresaId
        );

        if (!arquivo.validarFormato()) {
            throw new RegraNegocioException("Formato inválido para o tipo '" + arquivo.getTipoArquivo() + "'. A URL deve ter extensão compatível.");
        }

        arquivoDao.inserir(arquivo);
        return paraResponse(arquivo);
    }

    public ArquivoResponse atualizar(String id, ArquivoRequest request) {
        Arquivo existente = arquivoDao.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Arquivo com ID '" + id + "' não encontrado."));

        Arquivo atualizado = ArquivoFactory.criar(
                request.getTipo(),
                existente.getId(),
                request.getUrl().trim(),
                request.getPaginasLinhas(),
                existente.getEmpresaId()
        );
        atualizado.setDataUpload(existente.getDataUpload());

        if (!atualizado.validarFormato()) {
            throw new RegraNegocioException("Formato inválido para o tipo '" + atualizado.getTipoArquivo() + "'. A URL deve ter extensão compatível.");
        }

        arquivoDao.atualizar(atualizado);
        return paraResponse(atualizado);
    }

    public void excluir(String id) {
        if (!arquivoDao.existePorId(id)) {
            throw new EntidadeNaoEncontradaException("Arquivo com ID '" + id + "' não encontrado.");
        }
        arquivoDao.excluir(id);
    }

    private ArquivoResponse paraResponse(Arquivo a) {
        return new ArquivoResponse(
                a.getId(),
                a.getUrl(),
                a.getTipoArquivo(),
                a.getPaginasLinhas(),
                a.getEmpresaId(),
                a.converterParaTexto(),
                a.getDataUpload()
        );
    }
}
