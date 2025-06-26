package br.com.iftm.monitoria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class Monitoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "ID é obrigatório!")
    private Long id;

    @NotNull(message = "Status é obrigatório!")
    private String status;

    private String horario;

    @NotNull(message = "Ano é obrigatório!")
    private Integer ano;

    @NotNull(message = "Semestre é obrigatório!")
    private Integer semestre;

    @Column(name = "professor_id")
    private Integer professorId;

    @Column(name = "disciplina_id")
    private Integer disciplinaId;

    private String descricao;

    @Column(name = "monitor_id")
    private Integer monitorId;

    @ManyToOne
    @JoinColumn(name = "monitor_id", insertable = false, updatable = false)
    private Usuario monitor;

    @ManyToOne
    @JoinColumn(name = "disciplina_id", insertable = false, updatable = false)
    private Disciplina disciplina;

    @OneToOne
    @JoinColumn(name = "professor_id", insertable = false, updatable = false)
    private Usuario professor;

    // Construtor
    public Monitoria(String status, String horario, Integer ano, Integer semestre, Integer professorId, Integer disciplinaId, String descricao, Usuario monitor) {
        this.status = status;
        this.horario = horario;
        this.ano = ano;
        this.semestre = semestre;
        this.professorId = professorId;
        this.disciplinaId = disciplinaId;
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

    public Integer getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(Integer monitorId) {
        this.monitorId = monitorId;
    }

    public Integer getDisciplinaId() {
        return disciplinaId;
    }

    public void setDisciplinaId(Integer disciplinaId) {
        this.disciplinaId = disciplinaId;
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