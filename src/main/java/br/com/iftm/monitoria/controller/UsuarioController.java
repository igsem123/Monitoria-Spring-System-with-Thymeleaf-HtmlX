package br.com.iftm.monitoria.controller;

import br.com.iftm.monitoria.model.Papel;
import br.com.iftm.monitoria.model.Usuario;
import br.com.iftm.monitoria.service.PapelService;
import br.com.iftm.monitoria.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PapelService papelService;

    // Exibe a lista de usuários
    @GetMapping({"", "/listar"}) // Mapeia tanto a raiz quanto o /listar para a view de listagem
    public String listarUsuarios(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se o usuário está autenticado e se é um administrador ou monitor/professor
        boolean authenticated = authentication.isAuthenticated();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean isMonitorOrProfessor = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MONITOR") || authority.getAuthority().equals("ROLE_PROFESSOR"));

        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isMonitorOrProfessor", isMonitorOrProfessor);
        return "usuario/usuarios";
    }

    // Exibe o formulário de cadastro
    @GetMapping("/cadastrar")
    public String mostrarFormularioCadastro(Model model) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setPapel(new Papel());
        model.addAttribute("usuario", novoUsuario);
        model.addAttribute("papeis", papelService.listarTodos());
        return "usuario/cadastroUsuarios"; // Nome da View HTML
    }

    // Processa o envio do formulário de cadastro / edição
    @PostMapping // Lida tanto com o cadastro quanto com a atualização (se o ID estiver presente no @ModelAttribute)
    public String salvarUsuario(
            @ModelAttribute Usuario usuario,
            RedirectAttributes redirectAttributes,
            Principal principal
    ) {
        boolean isAuthenticated = principal != null;

        if(!isAuthenticated) {
            Papel papelMonitor = papelService.buscarPorNome("Monitor");
            usuario.setPapel(papelMonitor);
        }

        usuario.setAvatarPath("/images/avatars/default.png");
        usuarioService.salvar(usuario);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário salvo com sucesso!");

        if (isAuthenticated) {
            return "redirect:/usuarios/listar";
        }

        return  "redirect:/login";
    }

    // Exibe o formulário de edição para um usuário específico
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuarioExistente = usuarioService.buscarPorId(id);

        if (usuarioExistente == null) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Usuário não encontrado!");
            return "redirect:/usuarios/listar";
        }

        model.addAttribute("usuario", usuarioExistente);
        model.addAttribute("papeis", papelService.listarTodos());
        return "usuario/editarUsuario"; // Nome da View HTML
    }

    // Processa o envio do formulário de edição
    @PostMapping("/atualizar/{id}") // Lida com a atualização do usuário
    public String atualizarUsuario(@PathVariable Long id, @ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        usuarioService.atualizarUsuario(usuario);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário atualizado com sucesso!");
        return "redirect:/usuarios/listar";
    }

    // Exclui um usuário
    @PostMapping("/deletar/{id}")
    public String deletarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.deletar(id);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário deletado com sucesso!");
        return "redirect:/usuarios/listar";
    }
}
