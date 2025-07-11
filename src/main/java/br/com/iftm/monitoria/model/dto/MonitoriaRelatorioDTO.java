package br.com.iftm.monitoria.model.dto;

import br.com.iftm.monitoria.model.Monitoria;
import br.com.iftm.monitoria.model.Presenca;

import java.util.List;

public class MonitoriaRelatorioDTO {
    private String disciplina;
    private String monitor;
    private String professor;
    private String status;
    private Integer ano;
    private Integer semestre;
    private String horario;
    private List<PresencaDTO> presencas;

    public MonitoriaRelatorioDTO(Monitoria m, List<Presenca> presencas) {
        this.disciplina = m.getDisciplina() != null ? m.getDisciplina().getNome() : "";
        this.monitor = m.getMonitor() != null ? m.getMonitor().getNome() : "";
        this.professor = m.getProfessor() != null ? m.getProfessor().getNome() : "";
        this.status = m.getStatus() != null ? m.getStatus().name() : "";
        this.ano = m.getAno();
        this.semestre = m.getSemestre();
        this.horario = m.getHorario();
        this.presencas = presencas.stream()
                .filter(p -> p.getMonitoria().getId().equals(m.getId()))
                .map(PresencaDTO::new)
                .toList();
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
    public List<PresencaDTO> getPresencas() { return  presencas; }
}
