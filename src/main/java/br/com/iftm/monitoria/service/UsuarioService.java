package br.com.iftm.monitoria.service;

import br.com.iftm.monitoria.model.Usuario;
import br.com.iftm.monitoria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }
}
