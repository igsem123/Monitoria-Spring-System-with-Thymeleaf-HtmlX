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

        if (presencaList.size() < startItem) {
            presencaList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, presencaList.size());
            presencaList = presencaList.subList(startItem, toIndex);
        }

        return new PageImpl<>(presencaList, PageRequest.of(currentPage, pageSize), presencaList.size());
    }
}
