package br.com.iftm.monitoria.controller;

import br.com.iftm.monitoria.model.Disciplina;
import br.com.iftm.monitoria.service.DisciplinaService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/disciplinas")
public class DisciplinaController {

    @Autowired
    private DisciplinaService service;

    // Lista todas as disciplinas
    @GetMapping
    public String listarDisciplinas(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size
    ) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(10);

        Page<Disciplina> disciplinasPage = service.listarTodosPaginado(PageRequest.of(currentPage, pageSize));
        model.addAttribute("disciplinasPage", disciplinasPage);
        return "disciplina/listaDisciplinas";
    }

    @HxRequest
    @GetMapping("/gerar-codigo")
    public ResponseEntity<String> gerarCodigo(@RequestParam String nome) {
        try {
            String codigo = service.gerarCodigoDisciplina(nome);
            return ResponseEntity.ok(
                    "Código gerado: <span class='font-bold'>" + codigo + "</span>" +
                            "<script>document.getElementById('codigoInput').value = '" + codigo + "';</script>"
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("<span class='text-red-600 font-semibold'>" + e.getMessage() + "</span>");
        }
    }

    // Exibe o formulário de cadastro de disciplina
    @GetMapping("/cadastrar")
    public String mostrarFormularioCadastro(Model model) {
        Disciplina novaDisciplina = new Disciplina();
        model.addAttribute("disciplina", novaDisciplina);
        return "disciplina/cadastroDisciplina";
    }

    // Salva a disciplina enviada pelo formulário
    @PostMapping("/salvar")
    public String salvarDisciplina(
            @RequestParam("nome") String nome,
            @RequestParam("codigo") String codigo,
            RedirectAttributes redirectAttributes
    ) {
        if (nome == null || nome.trim().isEmpty() || codigo == null || codigo.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nome e código da disciplina são obrigatórios.");
            redirectAttributes.addFlashAttribute("disciplina", new Disciplina(nome, codigo)); // repopula
            return "redirect:/disciplinas/cadastrar";
        }

        try {
            Disciplina disciplina = new Disciplina();
            disciplina.setNome(nome);
            disciplina.setCodigo(codigo);

            service.salvar(disciplina);
            redirectAttributes.addFlashAttribute("successMessage", "Disciplina salva com sucesso!");
            return "redirect:/disciplinas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar disciplina: " + e.getMessage());
            redirectAttributes.addFlashAttribute("disciplina", new Disciplina(nome, codigo)); // repopula
            return "redirect:/disciplinas/cadastrar";
        }
    }

    // Exibe o formulário de edição de disciplina
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable("id") Long id, Model model) {
        Disciplina disciplina = service.buscarPorId(id);
        if (disciplina != null) {
            model.addAttribute("disciplina", disciplina);
            return "disciplina/editarDisciplina";
        } else {
            return "redirect:/disciplinas";
        }
    }

    // Atualiza a disciplina enviada pelo formulário
    @PostMapping("/atualizar")
    public String atualizarDisciplina(
            @ModelAttribute("disciplina") Disciplina disciplina,
            RedirectAttributes redirectAttributes
    ) {
        if (disciplina == null || disciplina.getId() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Disciplina inválida.");
            return "redirect:/disciplinas";
        }

        try {
            service.atualizar(disciplina);
            redirectAttributes.addFlashAttribute("successMessage", "Disciplina atualizada com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar disciplina: " + e.getMessage());
        }

        return "redirect:/disciplinas";
    }

    // Exclui uma disciplina
    @PostMapping("/excluir/{id}")
    public String excluirDisciplina(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            service.excluir(id);
            redirectAttributes.addFlashAttribute("successMessage", "Disciplina excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir disciplina: " + e.getMessage());
        }
        return "redirect:/disciplinas";
    }
}
