package br.com.iftm.monitoria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class Monitoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data da monitoria é obrigatória.")
    private LocalDate data;

    private String descricao;

    @ManyToOne
    private Usuario monitor;

    @ManyToOne
    private Disciplina disciplina;

    // Construtor
    public Monitoria(LocalDate data, String descricao, Usuario monitor, Disciplina disciplina) {
        this.data = data;
        this.descricao = descricao;
        this.monitor = monitor;
        this.disciplina = disciplina;
    }

    public Monitoria() {}

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Usuario getMonitor() {
        return monitor;
    }

    public void setMonitor(Usuario monitor) {
        this.monitor = monitor;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }
}