package br.com.iftm.monitoria.repository;

import br.com.iftm.monitoria.model.Monitoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoriaRepository extends JpaRepository<Monitoria, Long> {
}
