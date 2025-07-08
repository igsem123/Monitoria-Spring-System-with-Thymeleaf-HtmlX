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
}
