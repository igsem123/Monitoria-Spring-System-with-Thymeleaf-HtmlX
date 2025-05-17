package br.com.iftm.monitoria.repository;

import br.com.iftm.monitoria.model.Papel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PapelRepository extends JpaRepository<Papel, Long> {
}
