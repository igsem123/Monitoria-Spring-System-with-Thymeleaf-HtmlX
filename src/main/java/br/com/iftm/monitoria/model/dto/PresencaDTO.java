package br.com.iftm.monitoria.model.dto;

import br.com.iftm.monitoria.model.Presenca;

import java.time.LocalDate;

public class PresencaDTO {
    private LocalDate data;
    private int qtdAlunosPresentes;

    public PresencaDTO(Presenca p) {
        this.data = p.getData();
        this.qtdAlunosPresentes = p.getQtdAlunosPresentes();
    }

    // getters
    public LocalDate getData() {
        return data;
    }

    public int getQtdAlunosPresentes() {
        return qtdAlunosPresentes;
    }
}
