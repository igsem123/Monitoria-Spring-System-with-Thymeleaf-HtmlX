package br.com.iftm.monitoria.repository;

import br.com.iftm.monitoria.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByNome(String nome);
    Optional<Usuario> findByEmail(String email);
}
