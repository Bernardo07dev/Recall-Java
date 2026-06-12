package usuario;
import java.util.UUID;

public abstract class Usuario {
    private UUID id;
    private String nome;
    private String email;
    private String cargo;
    private String area;

    public Usuario(String nome, String email, String cargo, String area){
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.cargo = cargo;
        this.area = area;
    }

    public UUID getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public abstract Boolean podecriarcontexto();

    public void setArea(String area) {
        this.area = area;
    }
}
