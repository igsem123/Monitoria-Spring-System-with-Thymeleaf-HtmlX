package br.com.iftm.monitoria.controller;

import br.com.iftm.monitoria.model.Usuario;
import br.com.iftm.monitoria.service.UsuarioService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping
public class HomeController {

    @Autowired
    private UsuarioService usuarioService; // Injeção de dependência do UsuarioController, se necessário
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/login")
    public String login() {
        return "login"; // Nome da View HTML para a página de login
    }

    @PostMapping("/login-error")
    public String loginError() {
        return "redirect:/login?error"; // Redireciona para a página de login com um parâmetro de erro
    }

    @GetMapping("/index")
    public String index() {
        return "index"; // Nome da View HTML para a página inicial
    }

    @GetMapping("/perfil")
    public String perfil(
            Principal principal, // Obtém o usuário logado
            Model model
    ) {
        if (principal == null) {
            return "redirect:/login"; // Redireciona para a página de login
        }

        String email = principal.getName();
        Usuario usuarioLogado = usuarioService.buscarPorEmail(email);
        model.addAttribute("perfil", usuarioLogado);
        return "perfil"; // Nome da View HTML para a página de perfil do usuário
    }

    @HxRequest
    @GetMapping("/perfil/detalhe")
    public String perfilDetalheFragment(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        model.addAttribute("perfil", usuario);

        return "fragments/perfil-detalhe :: perfilDetalhe";
    }

    @HxRequest
    @GetMapping("/perfil/editar")
    public String editarPerfilForm(Principal principal, Model model) {
        if (principal == null) return "redirect:/login"; // cuidado aqui: redireciona a página inteira

        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        model.addAttribute("perfil", usuario);

        return "fragments/modal-editar-perfil :: fragment"; // nome correto do fragmento
    }

    @HxRequest
    @PostMapping("/perfil/editar")
    public String editarPerfilPost(
            @RequestParam("nome") String nome,
            @RequestParam("email") String email,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar,
            Principal principal,
            Model model
    ) {
        if (principal == null) return "redirect:/login";

        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        usuario.setNome(nome);
        usuario.setEmail(email);

        if (avatar != null && !avatar.isEmpty()) {
            try {
                String nomeArquivo = UUID.randomUUID() + "_" + avatar.getOriginalFilename();
                Path path = Paths.get("src/main/resources/static/images/avatar/" + nomeArquivo);
                Files.createDirectories(path.getParent());
                avatar.transferTo(path.toFile());
                usuario.setAvatarPath("/images/avatar/" + nomeArquivo); // salvando o caminho
            } catch (IOException e) {
                logger.error("Erro ao salvar avatar", e);
            }

            usuarioService.salvar(usuario);
            model.addAttribute("perfil", usuario);
        }

        // Fecha o modal e recarrega o conteúdo do perfil
        return "fragments/perfil-detalhe :: perfilDetalhe";
    }
}
