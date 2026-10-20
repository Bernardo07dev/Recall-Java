package br.com.fiap.recall.model;

public class Admin extends Usuario {

    public Admin() {
        super();
    }

    public Admin(String id, String nome, String email, String cargo, String area) {
        super(id, nome, email, cargo, area);
    }

    @Override
    public boolean podeCriarContexto() {
        return true;
    }

    @Override
    public String getTipoUsuario() {
        return "ADMIN";
    }
}
