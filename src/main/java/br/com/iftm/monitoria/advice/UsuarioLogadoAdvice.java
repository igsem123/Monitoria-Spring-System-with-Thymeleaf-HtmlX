package br.com.iftm.monitoria.advice;

import br.com.iftm.monitoria.model.Usuario;
import br.com.iftm.monitoria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UsuarioLogadoAdvice {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @ModelAttribute("usuarioLogado")
    public Usuario adicionarUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        String email = auth.getName(); // Obtém o email do usuário logado
        return usuarioRepository.findByEmail(email).orElse(null);
    }
}
