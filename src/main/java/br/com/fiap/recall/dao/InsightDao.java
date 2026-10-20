package br.com.fiap.recall.dao;

import br.com.fiap.recall.model.Insight;
import java.util.List;
import java.util.Optional;

public interface InsightDao {

    void inserir(Insight insight);

    Optional<Insight> buscarPorId(String id);

    List<Insight> listarPorEmpresa(String empresaId);

    void atualizar(Insight insight);

    void excluir(String id);

    boolean existePorId(String id);
}
