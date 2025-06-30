package br.com.iftm.monitoria.repository;

import br.com.iftm.monitoria.model.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresencaRepository extends JpaRepository<Presenca, Long> {
    void deleteByMonitoriaId(Long monitoriaId);
}
