package br.com.iftm.monitoria.service;

import br.com.iftm.monitoria.model.Monitoria;
import br.com.iftm.monitoria.repository.MonitoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitoriaService {

    @Autowired
    private MonitoriaRepository repository;

    public List<Monitoria> listarTodos() {
        return repository.findAll();
    }
}
