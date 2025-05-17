package br.com.iftm.monitoria.controller;

import br.com.iftm.monitoria.model.Usuario;
import br.com.iftm.monitoria.service.PapelService;
import br.com.iftm.monitoria.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PapelService papelService;

    // Local para adicionar métodos para lidar com requisições HTTP, como GET, POST, PUT, DELETE
     @GetMapping
     public String listarUsuarios(Model model) {
         List<Usuario> usuarios = usuarioService.listarTodos();
         model.addAttribute("usuarios", usuarios);
         return "usuarios";
     }

    @GetMapping("/cadastrar")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("papeis", papelService.listarTodos());
        return "cadastroUsuarios"; // Nome da sua view HTML
    }

    @PostMapping
    public String salvarUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.salvar(usuario);
        return "redirect:/usuarios"; // ou para onde quiser ir após salvar
    }
}
