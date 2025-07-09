package br.com.iftm.monitoria.service;

import br.com.iftm.monitoria.model.Monitoria;
import br.com.iftm.monitoria.model.Presenca;
import br.com.iftm.monitoria.repository.MonitoriaRepository;
import br.com.iftm.monitoria.repository.PresencaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PresencaService {

    @Autowired
    PresencaRepository repository;

    @Autowired
    MonitoriaRepository monitoriaRepository;

    public Page<Presenca> listarTodosPaginado(PageRequest pageable, Long monitorId) {
        List<Monitoria> monitoriaList = monitoriaRepository.findByMonitorId(monitorId);
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Presenca> presencaList = monitoriaList.stream()
                .flatMap(monitoria -> repository.findAllByMonitoria(monitoria).stream())
                .toList();

        System.out.println("Total de presencas: " + presencaList.size());

        if (presencaList.size() < startItem) {
            presencaList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, presencaList.size());
            presencaList = presencaList.subList(startItem, toIndex);
        }

        return new PageImpl<>(presencaList, PageRequest.of(currentPage, pageSize), presencaList.size());
    }

    public Optional<Presenca> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public void salvar(Presenca presenca) {
        if (presenca.getId() == null) {
            Monitoria monitoria = monitoriaRepository.findById(presenca.getMonitoria().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Monitoria não encontrada"));
            presenca.setMonitoria(monitoria);
        }

        // Verifico se a data é válida
        if (presenca.getData() == null) {
            throw new IllegalArgumentException("A data da presença não pode ser nula.");
        }

        // Verifico se já não existe uma presença para a mesma data e monitoria
        if (presenca.getId() != null) {
            Presenca existingPresenca = repository.findByDataAndMonitoria(presenca.getData(), presenca.getMonitoria());
            if (existingPresenca != null && !existingPresenca.getId().equals(presenca.getId())) {
                throw new IllegalArgumentException("Já existe uma presença registrada para esta data e monitoria.");
            }
        }

        // Salvo a presença
        repository.save(presenca);
    }
}
