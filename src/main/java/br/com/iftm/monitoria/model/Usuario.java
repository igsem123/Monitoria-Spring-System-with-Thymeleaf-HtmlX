package br.com.iftm.monitoria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório!")
    private String nome;

    @NotBlank(message = "Email é obrigatório!")
    private String email;

    @NotBlank(message = "Senha é obrigatória!")
    private String senha;

    @Column(name = "avatar_path")
    private String avatarPath;

    @ManyToOne
    @JoinColumn(name = "papel_id")
    private Papel papel;

    // Construtores
    public Usuario(String nome, String email, String senha, Papel papel) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.papel = papel;
    }

    public Usuario() {
        // Construtor vazio
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Papel getPapel() {
        return papel;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
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

    public void setAvatarPath(String path) {
        this.avatarPath = path;
    }

    public String getAvatarPath() {
        return avatarPath;
    }
}
