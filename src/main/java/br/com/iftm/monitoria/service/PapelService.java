package br.com.iftm.monitoria.service;

import br.com.iftm.monitoria.model.Papel;
import br.com.iftm.monitoria.repository.PapelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PapelService {
    @Autowired
    private PapelRepository repository;

    public List<Papel> listarTodos() {
        return repository.findAll();
    }

    public Papel buscarPorNome(String monitor) {
        return repository.findPapelByNome(monitor);
    }
}
