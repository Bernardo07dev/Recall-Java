package br.com.fiap.recall.dao;

import br.com.fiap.recall.model.Empresa;
import java.util.List;
import java.util.Optional;

public interface EmpresaDao {

    void inserir(Empresa empresa);

    Optional<Empresa> buscarPorId(String id);

    Optional<Empresa> buscarPorNome(String nome);

    List<Empresa> listarTodos();

    void atualizar(Empresa empresa);

    void excluir(String id);

    boolean existePorId(String id);

    boolean existePorNome(String nome);
}
