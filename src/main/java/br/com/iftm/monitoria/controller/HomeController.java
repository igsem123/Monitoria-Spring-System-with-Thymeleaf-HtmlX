package br.com.iftm.monitoria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class HomeController {

    @Autowired
    private UsuarioController usuarioController; // Injeção de dependência do UsuarioController, se necessário

    @RequestMapping("/login")
    public String login() {
        return "login"; // Nome da View HTML para a página de login
    }

    @PostMapping("/login-error")
    public String loginError() {
        return "redirect:/login?error"; // Redireciona para a página de login com um parâmetro de erro
    }

    @RequestMapping("/logout")
    public String logout() {
        return "redirect:/login?logout"; // Redireciona para a página de login com um parâmetro de logout
    }

    @GetMapping("/index")
    public String index() {
        return "index"; // Nome da View HTML para a página inicial
    }
}
