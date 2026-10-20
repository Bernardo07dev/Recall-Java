package br.com.fiap.recall.factory;

import br.com.fiap.recall.exception.RegraNegocioException;
import br.com.fiap.recall.model.Admin;
import br.com.fiap.recall.model.Manager;
import br.com.fiap.recall.model.Usuario;
import br.com.fiap.recall.model.Viewer;

public class UsuarioFactory {

    public static Usuario criar(String tipo, String id, String nome, String email, String cargo, String area) {
        if (tipo == null || tipo.isBlank()) {
            throw new RegraNegocioException("Tipo de usuário é obrigatório.");
        }

        switch (tipo.trim().toUpperCase()) {
            case "ADMIN":
                return new Admin(id, nome, email, cargo, area);
            case "MANAGER":
                return new Manager(id, nome, email, cargo, area);
            case "VIEWER":
                return new Viewer(id, nome, email, cargo, area);
            default:
                throw new RegraNegocioException("Tipo de usuário inválido: " + tipo + ". Utilize ADMIN, MANAGER ou VIEWER.");
        }
    }
}
