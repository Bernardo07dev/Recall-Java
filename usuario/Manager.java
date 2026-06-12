package usuario;

import java.util.ArrayList;
import java.util.List;

public class Manager extends Usuario {
    private List<Usuario> equipe;

    public Manager(String nome, String email) {
        super(nome, email, "Manager", "Gestão");
        this.equipe = new ArrayList<>();
    }

    @Override
    public Boolean podecriarcontexto() {
        return true;
    }

    public List<Usuario> getEquipe() {
        return this.equipe;
    }
}