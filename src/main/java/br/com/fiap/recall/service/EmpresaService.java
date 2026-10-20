package br.com.fiap.recall.service;

import br.com.fiap.recall.dao.ArquivoDao;
import br.com.fiap.recall.dao.EmpresaDao;
import br.com.fiap.recall.dao.InsightDao;
import br.com.fiap.recall.dto.request.EmpresaRequest;
import br.com.fiap.recall.dto.response.ArquivoResponse;
import br.com.fiap.recall.dto.response.EmpresaResponse;
import br.com.fiap.recall.dto.response.InsightResponse;
import br.com.fiap.recall.exception.EntidadeNaoEncontradaException;
import br.com.fiap.recall.exception.RegraNegocioException;
import br.com.fiap.recall.model.Arquivo;
import br.com.fiap.recall.model.Empresa;
import br.com.fiap.recall.model.Insight;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class EmpresaService {

    private final EmpresaDao empresaDao;
    private final ArquivoDao arquivoDao;
    private final InsightDao insightDao;

    public EmpresaService(EmpresaDao empresaDao, ArquivoDao arquivoDao, InsightDao insightDao) {
        this.empresaDao = empresaDao;
        this.arquivoDao = arquivoDao;
        this.insightDao = insightDao;
    }

    public List<EmpresaResponse> listarTodas() {
        List<Empresa> empresas = empresaDao.listarTodos();
        List<EmpresaResponse> respostas = new ArrayList<>();
        for (Empresa emp : empresas) {
            respostas.add(paraResponse(emp, false));
        }
        return respostas;
    }

    public EmpresaResponse buscarPorId(String id) {
        Empresa emp = empresaDao.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Empresa com ID '" + id + "' não encontrada."));
        return paraResponse(emp, true);
    }

    public EmpresaResponse cadastrar(EmpresaRequest request) {
        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new RegraNegocioException("O nome da empresa é obrigatório.");
        }
        if (request.getIndustry() == null || request.getIndustry().isBlank()) {
            throw new RegraNegocioException("O setor de atuação (industry) é obrigatório.");
        }

        if (empresaDao.existePorNome(request.getNome().trim())) {
            throw new RegraNegocioException("Já existe uma empresa cadastrada com o nome: " + request.getNome());
        }

        String id = UUID.randomUUID().toString();
        Empresa empresa = new Empresa(id, request.getNome().trim(), request.getIndustry().trim(),
                request.getSummarization(), request.getJsonIntel());

        empresaDao.inserir(empresa);
        return paraResponse(empresa, false);
    }

    public EmpresaResponse atualizar(String id, EmpresaRequest request) {
        Empresa existente = empresaDao.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Empresa com ID '" + id + "' não encontrada."));

        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new RegraNegocioException("O nome da empresa é obrigatório.");
        }
        if (request.getIndustry() == null || request.getIndustry().isBlank()) {
            throw new RegraNegocioException("O setor de atuação (industry) é obrigatório.");
        }

        if (!existente.getNome().equalsIgnoreCase(request.getNome().trim())
                && empresaDao.existePorNome(request.getNome().trim())) {
            throw new RegraNegocioException("Já existe outra empresa com o nome: " + request.getNome());
        }

        existente.setNome(request.getNome().trim());
        existente.setIndustry(request.getIndustry().trim());
        existente.setSummarization(request.getSummarization());
        existente.setJsonIntel(request.getJsonIntel());

        empresaDao.atualizar(existente);
        return paraResponse(existente, true);
    }

    public void excluir(String id) {
        if (!empresaDao.existePorId(id)) {
            throw new EntidadeNaoEncontradaException("Empresa com ID '" + id + "' não encontrada.");
        }
        empresaDao.excluir(id);
    }

    private EmpresaResponse paraResponse(Empresa emp, boolean carregarRelacionamentos) {
        List<ArquivoResponse> arquivosResp = new ArrayList<>();
        List<InsightResponse> insightsResp = new ArrayList<>();

        if (carregarRelacionamentos) {
            List<Arquivo> arquivos = arquivoDao.listarPorEmpresa(emp.getId());
            for (Arquivo arq : arquivos) {
                arquivosResp.add(new ArquivoResponse(
                        arq.getId(),
                        arq.getUrl(),
                        arq.getTipoArquivo(),
                        arq.getPaginasLinhas(),
                        arq.getEmpresaId(),
                        arq.converterParaTexto(),
                        arq.getDataUpload()
                ));
            }

            List<Insight> insights = insightDao.listarPorEmpresa(emp.getId());
            for (Insight ins : insights) {
                insightsResp.add(new InsightResponse(
                        ins.getId(),
                        ins.getCategoria().name(),
                        ins.getCriticidade().name(),
                        ins.getTitulo(),
                        ins.getInsight(),
                        ins.getAcaoSugerida(),
                        ins.gerarTaskJira(),
                        ins.getEmpresaId(),
                        ins.getDataCriacao()
                ));
            }
        }

        return new EmpresaResponse(
                emp.getId(),
                emp.getNome(),
                emp.getIndustry(),
                emp.getSummarization(),
                emp.getJsonIntel(),
                emp.getDataCriacao(),
                arquivosResp,
                insightsResp
        );
    }
}
