package br.com.iftm.monitoria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
public class Presenca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "É obrigatório informar a quantidade de alunos presentes.")
    @Column(name = "nr_alunos")
    private int qtdAlunosPresentes;

    @NotNull(message = "É obrigatório informar a data da monitoria.")
    private Date data;

    @ManyToOne
    @JoinColumn(name = "monitoria_id")
    private Monitoria monitoria;

    public Presenca() {
        // Construtor vazio
    }

    // Construtor com parâmetros
    public Presenca(int qtdAlunosPresentes, Date data, Monitoria monitoria) {
        this.qtdAlunosPresentes = qtdAlunosPresentes;
        this.data = data;
        this.monitoria = monitoria;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQtdAlunosPresentes() {
        return qtdAlunosPresentes;
    }

    public void setQtdAlunosPresentes(int qtdAlunosPresentes) {
        this.qtdAlunosPresentes = qtdAlunosPresentes;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Monitoria getMonitoria() {
        return monitoria;
    }

    public void setMonitoria(Monitoria monitoria) {
        this.monitoria = monitoria;
    }
}
