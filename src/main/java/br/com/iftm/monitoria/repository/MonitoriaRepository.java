package br.com.iftm.monitoria.repository;

import br.com.iftm.monitoria.model.Monitoria;
import br.com.iftm.monitoria.model.Presenca;
import br.com.iftm.monitoria.model.StatusMonitoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MonitoriaRepository extends JpaRepository<Monitoria, Long> {
    List<Monitoria> findByMonitorId(Long id);

    List<Monitoria> findByMonitorIdAndStatus(Long id, StatusMonitoria statusMonitoria);
}
