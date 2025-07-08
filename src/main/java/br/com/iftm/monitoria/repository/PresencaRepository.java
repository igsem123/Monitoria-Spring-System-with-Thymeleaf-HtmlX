package br.com.iftm.monitoria.repository;

import br.com.iftm.monitoria.model.Monitoria;
import br.com.iftm.monitoria.model.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PresencaRepository extends JpaRepository<Presenca, Long> {
    void deleteByMonitoriaId(Long monitoriaId);

    List<Presenca> findAllByMonitoria(Monitoria monitoria);
}
