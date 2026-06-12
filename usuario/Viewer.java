package usuario;

import java.util.ArrayList;
import java.util.List;

public class Viewer extends Usuario {

    private List<String> empresasVisiveis;

    public Viewer(String nome, String email) {
        super(nome, email, "Viewer", "Visualização");
        this.empresasVisiveis = new ArrayList<>();
    }

    @Override
    public Boolean podecriarcontexto() {
        return false;
    }

    public List<String> getEmpresasVisiveis() {
        return this.empresasVisiveis;
    }
}
