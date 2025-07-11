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

import java.time.LocalDate;
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

        // Lista completa de presenças do monitor
        List<Presenca> todasPresencas = monitoriaList.stream()
                .flatMap(monitoria -> repository.findAllByMonitoria(monitoria).stream())
                .toList();

        int total = todasPresencas.size();

        int start = (int) pageable.getOffset(); // startItem = currentPage * pageSize
        int end = Math.min((start + pageable.getPageSize()), total);

        List<Presenca> paginaPresencas;
        if (start > total) {
            paginaPresencas = Collections.emptyList();
        } else {
            paginaPresencas = todasPresencas.subList(start, end);
        }

        return new PageImpl<>(paginaPresencas, pageable, total);
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

        Integer ano = monitoriaRepository.findById(presenca.getMonitoria().getId()).isPresent()
                ? monitoriaRepository.findById(presenca.getMonitoria().getId()).get().getAno() : null;

        if (ano != null) {
            // Parse do Ano para LocalDate
            LocalDate inicioAno = LocalDate.of(ano, 1, 1);
            LocalDate fimAno = LocalDate.of(ano, 12, 31);

            if (presenca.getData().isBefore(inicioAno) || presenca.getData().isAfter(fimAno)) {
                throw new IllegalArgumentException("A data da lista de presença deve estar dentro do ano da monitoria.");
            }
        } else {
            throw new IllegalArgumentException("Ano da monitoria não encontrado.");
        }

        // Verifico se já não existe uma presença para a mesma data e monitoria
        if (presenca.getId() != null) {
            Presenca existingPresenca = repository.findByDataAndMonitoria(presenca.getData(), presenca.getMonitoria());
            if (existingPresenca != null && !existingPresenca.getId().equals(presenca.getId())) {
                throw new IllegalArgumentException("Já existe uma lista de presença registrada para esta data e monitoria.");
            }
        }

        // Salvo a presença
        repository.save(presenca);
    }

    public boolean excluir(Long presencaId) {
        try {
            repository.deleteById(presencaId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Presenca> buscarTodos() {
        return repository.findAll();
    }
}
