package br.com.iftm.monitoria.service;

import br.com.iftm.monitoria.model.Monitoria;
import br.com.iftm.monitoria.model.StatusMonitoria;
import br.com.iftm.monitoria.model.Usuario;
import br.com.iftm.monitoria.repository.MonitoriaRepository;
import br.com.iftm.monitoria.repository.PresencaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class MonitoriaService {

    @Autowired
    private MonitoriaRepository repository;

    @Autowired
    private PresencaRepository presencaRepository;

    /**
     * Lista todas as monitorias com paginação.
     * @param pageable
     * @return
     */
    public Page<Monitoria> listarTodosPaginado(Pageable pageable) {
        List<Monitoria> monitorias = repository.findAll()
            .stream()
            .sorted((m1, m2) -> m1.getId().compareTo(m2.getId()))
            .toList();

        int total = monitorias.size();

        int startItem = (int) pageable.getOffset();
        int end = Math.min((startItem + pageable.getPageSize()), total);

        List<Monitoria> paginaMonitorias;
        if (startItem > total) {
            paginaMonitorias = Collections.emptyList();
        } else {
            paginaMonitorias = monitorias.subList(startItem, end);
        }

        return new PageImpl<>(paginaMonitorias, pageable, total);
    }

    public Page<Monitoria> listarInscricoesPorUsuario(Usuario usuario, Pageable pageable) {
        if (usuario == null || usuario.getId() == null) {
            throw new RuntimeException("Usuário inválido!");
        }

        List<Monitoria> monitorias = repository.findByMonitorId(usuario.getId());
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        if (monitorias.size() < startItem) {
            monitorias = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, monitorias.size());
            monitorias = monitorias.subList(startItem, toIndex);
        }

        return new PageImpl<>(monitorias, PageRequest.of(currentPage, pageSize), monitorias.size());
    }

    public List<Monitoria> buscarMonitoriasDoMonitorAtivas(Usuario monitor) {
        return repository.findByMonitorIdAndStatus(monitor.getId(), StatusMonitoria.ATIVA);
    }

    public void salvar(Monitoria monitoria) {
        if (monitoria.getAno() == null || monitoria.getAno() <= 0) {
            throw new RuntimeException("Ano é obrigatório e deve ser maior que zero!");
        }

        if (monitoria.getSemestre() == null || monitoria.getSemestre() <= 0) {
            throw new RuntimeException("Semestre é obrigatório e deve ser maior que zero!");
        }

        if (monitoria.getDisciplina() == null || monitoria.getDisciplina().getId() == null) {
            throw new RuntimeException("Disciplina é obrigatória!");
        }

        if (monitoria.getMonitor() == null) {
            monitoria.setStatus(StatusMonitoria.ABERTA);
        } else {
            monitoria.setStatus(StatusMonitoria.ATIVA);
        }

        try {
            repository.save(monitoria);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar monitoria: " + e.getMessage(), e);
        }
    }

    public void editar(Monitoria monitoria) {
        if (monitoria.getAno() == null || monitoria.getAno() <= 0) {
            throw new RuntimeException("Ano é obrigatório e deve ser maior que zero!");
        }

        if (monitoria.getSemestre() == null || monitoria.getSemestre() <= 0) {
            throw new RuntimeException("Semestre é obrigatório e deve ser maior que zero!");
        }

        if (monitoria.getDisciplina() == null || monitoria.getDisciplina().getId() == null) {
            throw new RuntimeException("Disciplina é obrigatória!");
        }

        if (monitoria.getStatus() == null || monitoria.getStatus().toString().isEmpty()) {
            throw new RuntimeException("Status é obrigatório!");
        }

        try {
            repository.save(monitoria);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao editar monitoria: " + e.getMessage(), e);
        }
    }

    public void inscreverUsuario(Long id, Usuario usuario) {
        Monitoria monitoria = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monitoria não encontrada"));

        if (monitoria.getMonitor() != null) {
            throw new RuntimeException("Monitoria já possui um monitor!");
        }

        if (usuario == null || usuario.getId() == null) {
            throw new RuntimeException("Usuário inválido para inscrição!");
        }

        monitoria.setMonitor(usuario);
        monitoria.setStatus(StatusMonitoria.ATIVA);

        try {
            repository.save(monitoria);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inscrever usuário na monitoria: " + e.getMessage(), e);
        }
    }

    public void cancelarInscricao(Long id, Usuario usuario) {
        Monitoria monitoria = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monitoria não encontrada"));

        if (monitoria.getMonitor() == null || !monitoria.getMonitor().getId().equals(usuario.getId())) {
            throw new RuntimeException("Usuário não está inscrito nesta monitoria!");
        }

        monitoria.setMonitor(null);
        monitoria.setStatus(StatusMonitoria.ABERTA);

        try {
            repository.save(monitoria);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cancelar inscrição do usuário na monitoria: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deletar(Long id) {
        Monitoria monitoria = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monitoria não encontrada"));

        if (monitoria.getMonitor() != null && monitoria.getStatus() == StatusMonitoria.ATIVA) {
            throw new RuntimeException("Não é possível deletar uma monitoria que possui um monitor ativo!");
        }

        try {
            presencaRepository.deleteByMonitoriaId(id);
            repository.delete(monitoria);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar monitoria: " + e.getMessage(), e);
        }
    }

    public Monitoria buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monitoria não encontrada"));
    }

    public void encerrarMonitoria(Long id) {
        Monitoria monitoria = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monitoria não encontrada"));

        if (monitoria.getStatus() != StatusMonitoria.ATIVA) {
            throw new RuntimeException("Monitoria não está ativa para ser encerrada!");
        }

        monitoria.setStatus(StatusMonitoria.ENCERRADA);

        try {
            repository.save(monitoria);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao encerrar monitoria: " + e.getMessage(), e);
        }
    }

    public List<Monitoria> buscarTodos() {
        List<Monitoria> monitorias = repository.findAll();
        if (monitorias.isEmpty()) {
            throw new RuntimeException("Nenhuma monitoria encontrada.");
        }
        return monitorias;
    }
}
