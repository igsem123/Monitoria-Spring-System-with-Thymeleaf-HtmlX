package br.com.iftm.monitoria.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UsuarioLogado implements UserDetails {
    private final Usuario usuario;

    public UsuarioLogado(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getAvatarPath() {
        return usuario.getAvatarPath();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String papel = normalizarPapel(usuario.getPapel().getNome());
        return List.of(new SimpleGrantedAuthority("ROLE_" + papel));
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private String normalizarPapel(String papelNome) {
        return switch (papelNome.toLowerCase()) {
            case "administrador" -> "ADMIN";
            case "professor" -> "PROFESSOR";
            case "monitor" -> "MONITOR";
            default -> throw new IllegalArgumentException("Papel desconhecido: " + papelNome);
        };
    }
}
