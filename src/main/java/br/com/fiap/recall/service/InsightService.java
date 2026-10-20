package br.com.fiap.recall.service;

import br.com.fiap.recall.dao.EmpresaDao;
import br.com.fiap.recall.dao.InsightDao;
import br.com.fiap.recall.dto.request.InsightRequest;
import br.com.fiap.recall.dto.response.InsightResponse;
import br.com.fiap.recall.exception.EntidadeNaoEncontradaException;
import br.com.fiap.recall.exception.RegraNegocioException;
import br.com.fiap.recall.model.Categoria;
import br.com.fiap.recall.model.Criticidade;
import br.com.fiap.recall.model.Insight;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class InsightService {

    private final InsightDao insightDao;
    private final EmpresaDao empresaDao;

    public InsightService(InsightDao insightDao, EmpresaDao empresaDao) {
        this.insightDao = insightDao;
        this.empresaDao = empresaDao;
    }

    public List<InsightResponse> listarPorEmpresa(String empresaId) {
        if (!empresaDao.existePorId(empresaId)) {
            throw new EntidadeNaoEncontradaException("Empresa com ID '" + empresaId + "' não encontrada.");
        }

        List<Insight> insights = insightDao.listarPorEmpresa(empresaId);
        List<InsightResponse> responses = new ArrayList<>();
        for (Insight ins : insights) {
            responses.add(paraResponse(ins));
        }
        return responses;
    }

    public InsightResponse buscarPorId(String id) {
        Insight insight = insightDao.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Insight com ID '" + id + "' não encontrado."));
        return paraResponse(insight);
    }

    public InsightResponse cadastrar(String empresaId, InsightRequest request) {
        if (!empresaDao.existePorId(empresaId)) {
            throw new EntidadeNaoEncontradaException("Empresa com ID '" + empresaId + "' não encontrada.");
        }

        Categoria categoria = parseCategoria(request.getCategoria());
        Criticidade criticidade = parseCriticidade(request.getCriticidade());

        String id = UUID.randomUUID().toString();
        Insight insight = new Insight(
                id,
                categoria,
                criticidade,
                request.getTitulo().trim(),
                request.getInsight(),
                request.getAcaoSugerida(),
                empresaId
        );

        insightDao.inserir(insight);
        return paraResponse(insight);
    }

    public InsightResponse atualizar(String id, InsightRequest request) {
        Insight existente = insightDao.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Insight com ID '" + id + "' não encontrado."));

        Categoria categoria = parseCategoria(request.getCategoria());
        Criticidade criticidade = parseCriticidade(request.getCriticidade());

        existente.setCategoria(categoria);
        existente.setCriticidade(criticidade);
        existente.setTitulo(request.getTitulo().trim());
        existente.setInsight(request.getInsight());
        existente.setAcaoSugerida(request.getAcaoSugerida());

        insightDao.atualizar(existente);
        return paraResponse(existente);
    }

    public void excluir(String id) {
        if (!insightDao.existePorId(id)) {
            throw new EntidadeNaoEncontradaException("Insight com ID '" + id + "' não encontrado.");
        }
        insightDao.excluir(id);
    }

    private Categoria parseCategoria(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new RegraNegocioException("Categoria é obrigatória.");
        }
        try {
            return Categoria.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RegraNegocioException("Categoria inválida: " + valor + ". Utilize CHURN_RISK, UPSELL, COMPETITOR_THREAT, OPERACIONAL ou FEEDBACK_PRODUTO.");
        }
    }

    private Criticidade parseCriticidade(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new RegraNegocioException("Criticidade é obrigatória.");
        }
        try {
            return Criticidade.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RegraNegocioException("Criticidade inválida: " + valor + ". Utilize BAIXA, MEDIA ou ALTA.");
        }
    }

    private InsightResponse paraResponse(Insight ins) {
        return new InsightResponse(
                ins.getId(),
                ins.getCategoria().name(),
                ins.getCriticidade().name(),
                ins.getTitulo(),
                ins.getInsight(),
                ins.getAcaoSugerida(),
                ins.gerarTaskJira(),
                ins.getEmpresaId(),
                ins.getDataCriacao()
        );
    }
}
