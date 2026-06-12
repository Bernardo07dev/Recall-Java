package insight;

import java.util.UUID;

public class Insight {
    private UUID id;
    private Categoria categoria;
    private Criticidade criticidade;
    private String titulo;
    private String descricao;
    private String acaoSugerida;
    private UUID empresaId;

    public Insight(Categoria categoria, Criticidade criticidade, String titulo) {
        this.id = UUID.randomUUID();
        this.categoria = categoria;
        this.criticidade = criticidade;
        this.titulo = titulo;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public Criticidade getCriticidade() {
        return this.criticidade;
    }

    public void setCriticidade(Criticidade c) {
        this.criticidade = c;
    }

    public String gerarTaskJira() {
        return "Jira Task criada para o insight: " + this.titulo;
    }
}
