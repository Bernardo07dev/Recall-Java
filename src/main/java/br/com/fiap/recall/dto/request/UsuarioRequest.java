package br.com.fiap.recall.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioRequest {

    @NotBlank(message = "O nome do usuário é obrigatório.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Formato de e-mail inválido.")
    @Size(max = 120, message = "O e-mail deve ter no máximo 120 caracteres.")
    private String email;

    @NotBlank(message = "O cargo é obrigatório.")
    @Size(max = 50, message = "O cargo deve ter no máximo 50 caracteres.")
    private String cargo;

    @NotBlank(message = "A área é obrigatória.")
    @Size(max = 50, message = "A área deve ter no máximo 50 caracteres.")
    private String area;

    @NotBlank(message = "O tipo de usuário é obrigatório (ADMIN, MANAGER ou VIEWER).")
    private String tipo;

    public UsuarioRequest() {
    }

    public UsuarioRequest(String nome, String email, String cargo, String area, String tipo) {
        this.nome = nome;
        this.email = email;
        this.cargo = cargo;
        this.area = area;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
