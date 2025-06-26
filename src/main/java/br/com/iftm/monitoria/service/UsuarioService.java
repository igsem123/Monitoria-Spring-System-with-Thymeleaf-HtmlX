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
        if(usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            throw new RuntimeException("Senha é obrigatória!");
        } else if(usuario.getPapel() == null) {
            throw new RuntimeException("Papel é obrigatório!");
        } else if(usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new RuntimeException("Email é obrigatório!");
        }

        var senhaEncriptada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaEncriptada);
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

        existente.setPapel(atualizado.getPapel());
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
