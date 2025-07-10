package br.com.iftm.monitoria.model.dto;

import br.com.iftm.monitoria.model.Monitoria;

public class MonitoriaRelatorioDTO {
    private String disciplina;
    private String monitor;
    private String professor;
    private String status;
    private Integer ano;
    private Integer semestre;
    private String horario;

    public MonitoriaRelatorioDTO(Monitoria m) {
        this.disciplina = m.getDisciplina() != null ? m.getDisciplina().getNome() : "";
        this.monitor = m.getMonitor() != null ? m.getMonitor().getNome() : "";
        this.professor = m.getProfessor() != null ? m.getProfessor().getNome() : "";
        this.status = m.getStatus() != null ? m.getStatus().name() : "";
        this.ano = m.getAno();
        this.semestre = m.getSemestre();
        this.horario = m.getHorario();
    }

    // Getters
    public String getDisciplina() {
        return disciplina;
    }

    public String getMonitor() {
        return monitor;
    }

    public String getProfessor() {
        return professor;
    }

    public String getStatus() {
        return status;
    }

    public Integer getAno() {
        return ano;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public String getHorario() {
        return horario;
    }
}
