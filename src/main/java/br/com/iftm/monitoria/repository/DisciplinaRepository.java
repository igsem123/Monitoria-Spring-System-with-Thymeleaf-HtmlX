package br.com.iftm.monitoria.repository;

import br.com.iftm.monitoria.model.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
}
