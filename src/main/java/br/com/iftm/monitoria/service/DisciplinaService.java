package br.com.iftm.monitoria.service;

import br.com.iftm.monitoria.model.Disciplina;
import br.com.iftm.monitoria.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplinaService {

    @Autowired
    private DisciplinaRepository repository;

    public List<Disciplina> listarTodos() {
        return repository.findAll();
    }

    // Método para salvar disciplina
    public void salvar(Disciplina disciplina) {
        repository.save(disciplina);
    }
}
