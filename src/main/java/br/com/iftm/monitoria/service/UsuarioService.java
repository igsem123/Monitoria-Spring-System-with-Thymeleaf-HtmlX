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

    public void salvar(Usuario usuario) {
        repository.save(usuario);
    }

    public void atualizarUsuario(Usuario atualizado) {
        Usuario existente = repository.findById(atualizado.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        existente.setNome(atualizado.getNome());
        existente.setEmail(atualizado.getEmail());

        if (atualizado.getSenha() != null && !atualizado.getSenha().isBlank()) {
            existente.setSenha(atualizado.getSenha());
        }

        repository.save(existente);
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public List<Usuario> buscarPorNome(String nome) {
        return repository.findByNome(nome);
    }
}
