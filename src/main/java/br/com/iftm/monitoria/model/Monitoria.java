package br.com.iftm.monitoria.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Monitoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;
    private String descricao;

    @ManyToOne
    private Usuario monitor;

    @ManyToOne
    private Disciplina disciplina;
    
}