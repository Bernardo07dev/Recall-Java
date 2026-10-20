package br.com.fiap.recall.service;

import br.com.fiap.recall.dao.UsuarioDao;
import br.com.fiap.recall.dto.request.UsuarioRequest;
import br.com.fiap.recall.dto.response.UsuarioResponse;
import br.com.fiap.recall.exception.EntidadeNaoEncontradaException;
import br.com.fiap.recall.exception.RegraNegocioException;
import br.com.fiap.recall.factory.UsuarioFactory;
import br.com.fiap.recall.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioDao usuarioDao;

    public UsuarioService(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public List<UsuarioResponse> listarTodos() {
        List<Usuario> usuarios = usuarioDao.listarTodos();
        List<UsuarioResponse> responses = new ArrayList<>();
        for (Usuario u : usuarios) {
            responses.add(paraResponse(u));
        }
        return responses;
    }

    public UsuarioResponse buscarPorId(String id) {
        Usuario usuario = usuarioDao.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário com ID '" + id + "' não encontrado."));
        return paraResponse(usuario);
    }

    public UsuarioResponse cadastrar(UsuarioRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RegraNegocioException("E-mail é obrigatório.");
        }

        if (usuarioDao.existePorEmail(request.getEmail().trim())) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com o e-mail: " + request.getEmail());
        }

        String id = UUID.randomUUID().toString();
        Usuario usuario = UsuarioFactory.criar(
                request.getTipo(),
                id,
                request.getNome().trim(),
                request.getEmail().trim().toLowerCase(),
                request.getCargo().trim(),
                request.getArea().trim()
        );

        usuarioDao.inserir(usuario);
        return paraResponse(usuario);
    }

    public UsuarioResponse atualizar(String id, UsuarioRequest request) {
        Usuario existente = usuarioDao.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário com ID '" + id + "' não encontrado."));

        if (!existente.getEmail().equalsIgnoreCase(request.getEmail().trim())
                && usuarioDao.existePorEmail(request.getEmail().trim())) {
            throw new RegraNegocioException("Já existe outro usuário cadastrado com o e-mail: " + request.getEmail());
        }

        Usuario atualizado = UsuarioFactory.criar(
                request.getTipo(),
                existente.getId(),
                request.getNome().trim(),
                request.getEmail().trim().toLowerCase(),
                request.getCargo().trim(),
                request.getArea().trim()
        );
        atualizado.setDataCriacao(existente.getDataCriacao());

        usuarioDao.atualizar(atualizado);
        return paraResponse(atualizado);
    }

    public void excluir(String id) {
        if (!usuarioDao.existePorId(id)) {
            throw new EntidadeNaoEncontradaException("Usuário com ID '" + id + "' não encontrado.");
        }
        usuarioDao.excluir(id);
    }

    private UsuarioResponse paraResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCargo(),
                usuario.getArea(),
                usuario.getTipoUsuario(),
                usuario.podeCriarContexto(),
                usuario.getDataCriacao()
        );
    }
}
