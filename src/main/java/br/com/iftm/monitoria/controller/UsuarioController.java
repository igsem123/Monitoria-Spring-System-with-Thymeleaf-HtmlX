package br.com.iftm.monitoria.controller;

import br.com.iftm.monitoria.model.Papel;
import br.com.iftm.monitoria.model.Usuario;
import br.com.iftm.monitoria.service.PapelService;
import br.com.iftm.monitoria.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        return "usuarios";
    }

    // Exibe o formulário de cadastro
    @GetMapping("/cadastrar")
    public String mostrarFormularioCadastro(Model model) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setPapel(new Papel());
        model.addAttribute("usuario", novoUsuario);
        model.addAttribute("papeis", papelService.listarTodos());
        return "cadastroUsuarios"; // Nome da View HTML
    }

    // Processa o envio do formulário de cadastro / edição
    @PostMapping // Lida tanto com o cadastro quanto com a atualização (se o ID estiver presente no @ModelAttribute)
    public String salvarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        usuarioService.salvar(usuario);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário salvo com sucesso!");
        return "redirect:/usuarios/listar";
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
        return "editarUsuario"; // Nome da View HTML
    }

    // Processa o envio do formulário de edição
    @PostMapping("/{id}") // Lida com a atualização do usuário
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
