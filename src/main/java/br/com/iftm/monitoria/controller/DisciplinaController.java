package br.com.iftm.monitoria.controller;

import br.com.iftm.monitoria.model.Disciplina;
import br.com.iftm.monitoria.service.DisciplinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/disciplinas")
public class DisciplinaController {

    @Autowired
    private DisciplinaService service;

    @GetMapping
    public String listarDisciplinas(Model model) {
        List<Disciplina> disciplinas = service.listarTodos();
        model.addAttribute("disciplinas", disciplinas);
        return "disciplinas";
    }

    // Exibe o formulário de cadastro de disciplina
    @GetMapping("/cadastrar")
    public String mostrarFormularioCadastro(Model model) {
        Disciplina novaDisciplina = new Disciplina();
        model.addAttribute("disciplina", novaDisciplina);
        return "cadastroDisciplina"; // Nome da View HTML para o formulário
    }
}
