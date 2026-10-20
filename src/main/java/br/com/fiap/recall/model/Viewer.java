package br.com.fiap.recall.model;

public class Viewer extends Usuario {

    public Viewer() {
        super();
    }

    public Viewer(String id, String nome, String email, String cargo, String area) {
        super(id, nome, email, cargo, area);
    }

    @Override
    public boolean podeCriarContexto() {
        return false;
    }

    @Override
    public String getTipoUsuario() {
        return "VIEWER";
    }
}
