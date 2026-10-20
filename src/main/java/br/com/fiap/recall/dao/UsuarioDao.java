package br.com.fiap.recall.dao;

import br.com.fiap.recall.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioDao {

    void inserir(Usuario usuario);

    Optional<Usuario> buscarPorId(String id);

    Optional<Usuario> buscarPorEmail(String email);

    List<Usuario> listarTodos();

    void atualizar(Usuario usuario);

    void excluir(String id);

    boolean existePorId(String id);

    boolean existePorEmail(String email);
}
