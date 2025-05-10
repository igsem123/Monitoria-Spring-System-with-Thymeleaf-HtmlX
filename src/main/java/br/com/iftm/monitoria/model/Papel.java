package br.com.iftm.monitoria.model;

import jakarta.persistence.*;

@Entity
public class Papel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
}
