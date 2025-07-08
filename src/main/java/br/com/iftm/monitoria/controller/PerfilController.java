package br.com.iftm.monitoria.controller;

import br.com.iftm.monitoria.model.Usuario;
import br.com.iftm.monitoria.service.UsuarioService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    UsuarioService usuarioService;

    // Simulação de uma lista de avatares disponíveis
    private static final List<String> AVATARES_DISPONIVEIS = List.of(
            "/images/avatars/student.png",
            "/images/avatars/student-2.png",
            "/images/avatars/student-3.png",
            "/images/avatars/student-4.png",
            "/images/avatars/student-5.png",
            "/images/avatars/student-6.png",
            "/images/avatars/student-7.png",
            "/images/avatars/student-8.png",
            "/images/avatars/student-9.png",
            "/images/avatars/student-10.png"
    );

    private static final Logger logger = LoggerFactory.getLogger(PerfilController.class);

    /**
     * Endpoint para exibir o perfil do usuário.
     * @param principal
     * @param model
     * @return
     */
    @GetMapping
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

    /**
     * Endpoint para exibir o fragmento do perfil do usuário.
     * @param principal
     * @param model
     * @return
     */
    @HxRequest
    @GetMapping("/detalhe")
    public String perfilDetalheFragment(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        model.addAttribute("perfil", usuario);

        return "fragments/perfil-detalhe :: perfilDetalhe";
    }

    /**
     * Endpoint para exibir o fragmento do avatar do usuário.
     * @param principal
     * @param model
     * @return
     */
    @HxRequest
    @GetMapping("/editar")
    public String editarPerfilForm(Principal principal, Model model) {
        if (principal == null) return "redirect:/login"; // cuidado aqui: redireciona a página inteira

        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        model.addAttribute("perfil", usuario);

        return "fragments/modal-editar-perfil :: fragment"; // nome correto do fragmento
    }

    /**
     * Endpoint para editar o perfil do usuário.
     * @param nome
     * @param email
     * @param avatarUrl
     * @param principal
     * @param model
     * @return
     */
    @HxRequest
    @PostMapping("/editar")
    public String editarPerfilPost(
            @RequestParam("nome") String nome,
            @RequestParam("email") String email,
            @RequestParam(value = "avatarUrl", required = false) String avatarUrl,
            Principal principal,
            Model model
    ) {
        if (principal == null) return "redirect:/login";

        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        usuario.setNome(nome);
        usuario.setEmail(email);

        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            usuario.setAvatarPath(avatarUrl); // Salvando o caminho
        }

        // Salva e adiciona no model
        usuarioService.atualizarUsuario(usuario);
        model.addAttribute("perfil", usuario);

        // Fecha o modal e recarrega o conteúdo do perfil
        return "fragments/perfil-completo :: multiAtualizacao";
    }

    /**
     * Endpoint que retorna o fragmento HTML do modal.
     */
    @HxRequest
    @GetMapping("/ui/modal-avatar")
    public String getModalAvatar(Model model) {
        // Adiciona a lista de avatares ao modelo para o Thymeleaf renderizar
        model.addAttribute("avatares", AVATARES_DISPONIVEIS);
        // Retorna o caminho para o fragmento do modal
        return "fragments/modal-avatar-pre-selecionado :: fragment";
    }

    /**
     * Endpoint que processa a seleção do avatar, salva no banco (lógica omitida)
     * e retorna o HTML atualizado do display do avatar.
     */
    @HxRequest
    @PostMapping("/avatar/selecionar")
    public String selecionarAvatar(
            @RequestParam String avatarUrl,
            Model model,
            Principal principal
    ) {

        System.out.println("Avatar selecionado: " + avatarUrl);

        if (principal == null) {
            return "redirect:/login"; // Redireciona para a página de login se o usuário não estiver autenticado
        }

        String email = principal.getName();
        Usuario usuarioParaAtualizar = usuarioService.buscarPorEmail(email);
        usuarioParaAtualizar.setAvatarPath(avatarUrl);
        usuarioService.atualizarAvatar(usuarioParaAtualizar);

        model.addAttribute("perfil", usuarioParaAtualizar);

        // Retorna um fragmento HTML que irá substituir o #avatar-display na página.
        return "fragments/avatar-display :: avatarDisplay";
    }
}