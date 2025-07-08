package br.com.iftm.monitoria.service;

import br.com.iftm.monitoria.model.Usuario;
import br.com.iftm.monitoria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public void salvar(Usuario usuario) {
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            throw new RuntimeException("Senha é obrigatória!");
        }
        if (usuario.getPapel() == null) {
            throw new RuntimeException("Papel é obrigatório!");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new RuntimeException("Email é obrigatório!");
        }

        if (repository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado!");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        try {
            repository.save(usuario);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar usuário: " + e.getMessage(), e);
        }
    }

    public void atualizarUsuario(Usuario atualizado) {
        try {
            Usuario existente = repository.findById(atualizado.getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            existente.setNome(atualizado.getNome());
            existente.setEmail(atualizado.getEmail());

            if (atualizado.getSenha() != null && !atualizado.getSenha().isBlank()) {
                existente.setSenha(passwordEncoder.encode(atualizado.getSenha()));
            }

            existente.setPapel(atualizado.getPapel());
            repository.save(existente);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    public Usuario buscarPorId(Long id) {
        try {
            if (id == null) {
                throw new RuntimeException("ID não pode ser nulo.");
            }

            return repository.findById(id).orElse(null);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao buscar usuário: " + e.getMessage());
        }
    }

    public void deletar(Long id) {
        try {
            Usuario usuario = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            if (usuario.getPapel().getNome().equalsIgnoreCase("ADMINISTRADOR")) {
                throw new RuntimeException("Não é possível excluir um usuário com papel de administrador.");
            }

            repository.deleteById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao excluir usuário: " + e.getMessage());
        }
    }

    public List<Usuario> buscarPorNome(String nome) {
        return repository.findByNome(nome);
    }

    public Usuario buscarPorEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email não pode ser nulo ou vazio.");
        }
        return repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário com email " + email + " não encontrado."));
    }
}
