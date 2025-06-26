package br.com.iftm.monitoria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da disciplina é obrigatório.")
    private String nome;

    // Construtor
    public Disciplina() {}

    public Disciplina(String nome) {
        this.nome = nome;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }
}