package br.com.fiap.recall.dao;

import br.com.fiap.recall.model.Arquivo;
import java.util.List;
import java.util.Optional;

public interface ArquivoDao {

    void inserir(Arquivo arquivo);

    Optional<Arquivo> buscarPorId(String id);

    List<Arquivo> listarPorEmpresa(String empresaId);

    void atualizar(Arquivo arquivo);

    void excluir(String id);

    boolean existePorId(String id);
}
