package empresa;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import arquivo.Arquivo;

public class Empresa {
    private UUID id;
    private String nome;
    private String industry;
    private String summarization;
    private String json;
    private List<Arquivo> arquivos;

    public Empresa(String nome, String industry) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.industry = industry;
        this.arquivos = new ArrayList<>();
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getJson() {
        return this.json;
    }

    public void adicionarArquivo(Arquivo a) {
        this.arquivos.add(a);
    }
}
