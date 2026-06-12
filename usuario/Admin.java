package usuario;

public class Admin extends Usuario {
    private Integer nivelAcesso;

    public Admin(String nome, String email) {
        super(nome, email, "Admin", "Administração");
        this.nivelAcesso = 1;
    }

    @Override
    public Boolean podecriarcontexto() {
        return true;
    }

    public void gerenciarUsuarios() {
        System.out.println("Abrindo painel de gerenciamento de usuários para o Admin: " + getNome());
    }
}