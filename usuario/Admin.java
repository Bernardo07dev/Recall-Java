package usuario;

public class Admin extends Usuario{
    public Admin(String nome, String email) {
        super(nome, email, "Admin", "Administração");
    }

    @Override
    public Boolean podecriarcontexto() {
        return true;
    }
}