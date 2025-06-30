package br.com.iftm.monitoria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Monitoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Status é obrigatório!")
    private String status;

    private String horario;

    @NotNull(message = "Ano é obrigatório!")
    private Integer ano;

    @NotNull(message = "Semestre é obrigatório!")
    private Integer semestre;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "monitor_id")
    private Usuario monitor;

    @ManyToOne
    @JoinColumn(name = "disciplina_id")
    private Disciplina disciplina;

    @OneToOne
    @JoinColumn(name = "professor_id")
    private Usuario professor;

    // Construtor
    public Monitoria(String status, String horario, Integer ano, Integer semestre, Integer professorId, Integer disciplinaId, String descricao, Usuario monitor) {
        this.status = status;
        this.horario = horario;
        this.ano = ano;
        this.semestre = semestre;
        this.descricao = descricao;
        this.monitor = monitor;
    }

    public Monitoria() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Usuario getProfessor() {
        return professor;
    }

    public void setProfessor(Usuario professor) {
        this.professor = professor;
    }
}