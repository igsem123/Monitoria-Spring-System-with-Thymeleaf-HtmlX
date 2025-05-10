package br.com.iftm.monitoria.controller;

import br.com.iftm.monitoria.model.Usuario;
import br.com.iftm.monitoria.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    // Aqui você pode adicionar métodos para lidar com requisições HTTP, como GET, POST, PUT, DELETE
    // Exemplo:
     @GetMapping
     public String listarUsuarios(Model model) {
         List<Usuario> usuarios = service.listarTodos();
         model.addAttribute("usuarios", usuarios);
         return "usuarios";
     }
}
