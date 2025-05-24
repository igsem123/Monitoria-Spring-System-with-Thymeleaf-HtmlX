package br.com.iftm.monitoria.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    // Relacionamento reverso (opcional)
    @OneToMany(mappedBy = "disciplina")
    private List<Monitoria> monitorias;
}