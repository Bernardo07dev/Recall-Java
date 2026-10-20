package br.com.fiap.recall.model;

import java.time.LocalDateTime;

public abstract class Usuario {

    private String id;
    private String nome;
    private String email;
    private String cargo;
    private String area;
    private LocalDateTime dataCriacao;

    public Usuario() {
    }

    public Usuario(String id, String nome, String email, String cargo, String area) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cargo = cargo;
        this.area = area;
        this.dataCriacao = LocalDateTime.now();
    }

    public abstract boolean podeCriarContexto();

    public abstract String getTipoUsuario();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
