package br.com.iftm.monitoria.service;

import br.com.iftm.monitoria.model.Monitoria;
import br.com.iftm.monitoria.repository.MonitoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MonitoriaService {

    @Autowired
    private MonitoriaRepository repository;

    public List<Monitoria> listarTodos() {
        return repository.findAll();
    }

    public Page<Monitoria> listarTodosPaginado(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Monitoria> monitorias = repository.findAll();

        if (monitorias.size() < startItem) {
            monitorias = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, monitorias.size());
            monitorias = monitorias.subList(startItem, toIndex);
        }

        return new PageImpl<>(monitorias, PageRequest.of(currentPage, pageSize), monitorias.size());
    }
}
