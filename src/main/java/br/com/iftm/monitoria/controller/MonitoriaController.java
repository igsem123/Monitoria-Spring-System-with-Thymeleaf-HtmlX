package br.com.iftm.monitoria.controller;

import br.com.iftm.monitoria.model.Disciplina;
import br.com.iftm.monitoria.model.Monitoria;
import br.com.iftm.monitoria.model.StatusMonitoria;
import br.com.iftm.monitoria.model.Usuario;
import br.com.iftm.monitoria.service.DisciplinaService;
import br.com.iftm.monitoria.service.MonitoriaService;
import br.com.iftm.monitoria.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/monitorias")
public class MonitoriaController {

    @Autowired
    private MonitoriaService service;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DisciplinaService disciplinaService;

    @GetMapping("")
    public String listarMonitorias(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size
    ) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<Monitoria> monitoriasPage = service.listarTodosPaginado(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("monitoriasPage", monitoriasPage);

        int totalPages = monitoriasPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();

            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "monitoria/listaMonitorias"; // Nome da View HTML para a listagem de monitorias
    }

    @GetMapping("/inscricoes")
    public String listarInscricoes(
            Model model,
            Principal principal,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size
    ) {
        if (principal == null) {
            return "redirect:/login"; // Redireciona para a página de login se o usuário não estiver autenticado
        }

        String email = principal.getName();
        Usuario usuario = usuarioService.buscarPorEmail(email);

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<Monitoria> monitoriasInscritas = service.listarInscricoesPorUsuario(usuario, PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("monitoriasInscritas", monitoriasInscritas);

        int totalPages = monitoriasInscritas.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "monitoria/listaInscricoes"; // Nome da View HTML para a lista de inscrições do usuário
    }

    @PostMapping
    public String cadastrarMonitoria(@ModelAttribute Monitoria monitoria, RedirectAttributes redirectAttributes) {
        service.salvar(monitoria);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Monitoria cadastrada com sucesso!");
        return "redirect:/monitorias"; // Redireciona para a lista de monitorias após o cadastro
    }

    @GetMapping("/cadastrar")
    public String mostrarFormularioCadastro(Model model) {
        Monitoria novaMonitoria = new Monitoria();
        novaMonitoria.setDisciplina(new Disciplina());
        novaMonitoria.setMonitor(new Usuario());
        novaMonitoria.setProfessor(new Usuario());

        List<Usuario> professores = usuarioService.listarTodos().stream()
                .filter(usuario -> "Professor".equals(usuario.getPapel().getNome()))
                .toList();
        model.addAttribute("professores", professores);

        List<Usuario> monitores = usuarioService.listarTodos().stream()
                .filter(usuario -> "Monitor".equals(usuario.getPapel().getNome()))
                .toList();
        model.addAttribute("monitores", monitores);

        List<Disciplina> disciplinas = disciplinaService.listarTodos();
        model.addAttribute("disciplinas", disciplinas);

        model.addAttribute("monitoria", novaMonitoria);
        return "monitoria/cadastroMonitorias"; // Nome da View HTML para o formulário de cadastro
    }

    @PostMapping("/deletar/{id}")
    public String deletarMonitoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.deletar(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Monitoria deletada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao deletar monitoria: " + e.getMessage());
        }
        return "redirect:/monitorias"; // Redireciona para a lista de monitorias após a exclusão
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        Monitoria monitoria = service.buscarPorId(id);
        if (monitoria == null) {
            return "redirect:/monitorias"; // Redireciona se a monitoria não for encontrada
        }

        List<Usuario> professores = usuarioService.listarTodos().stream()
                .filter(usuario -> "Professor".equals(usuario.getPapel().getNome()))
                .toList();
        model.addAttribute("professores", professores);

        List<Usuario> monitores = usuarioService.listarTodos().stream()
                .filter(usuario -> "Monitor".equals(usuario.getPapel().getNome()))
                .toList();
        model.addAttribute("monitores", monitores);

        List<Disciplina> disciplinas = disciplinaService.listarTodos();
        model.addAttribute("disciplinas", disciplinas);

        model.addAttribute("statusMonitorias", StatusMonitoria.values());

        model.addAttribute("monitoria", monitoria);
        return "monitoria/edicaoMonitorias"; // Nome da View HTML para o formulário de edição
    }

    @PostMapping("/editar/{id}")
    public String editarMonitoria(@ModelAttribute Monitoria monitoria, RedirectAttributes redirectAttributes) {
        try {
            service.editar(monitoria);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Monitoria editada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao editar monitoria: " + e.getMessage());
        }
        return "redirect:/monitorias"; // Redireciona para a lista de monitorias após a edição
    }

    @PostMapping("/inscrever/{id}")
    public String inscrever(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Redireciona para a página de login se o usuário não estiver autenticado
        }
        String email = principal.getName();
        Usuario usuario = usuarioService.buscarPorEmail(email);
        service.inscreverUsuario(id, usuario);
        return "redirect:/monitorias";
    }

    @PostMapping("/cancelarInscricao/{id}")
    public String cancelarInscricao(@PathVariable Long id, Principal principal) {
        if (principal == null) { return "redirect:/login"; }
        String email = principal.getName();
        Usuario usuario = usuarioService.buscarPorEmail(email);
        service.cancelarInscricao(id, usuario);
        return "redirect:/monitorias";
    }

    @GetMapping("/visualizar/{id}")
    public String visualizarMonitoria(@PathVariable Long id, Model model, @AuthenticationPrincipal Usuario usuario) {
        Monitoria monitoria = service.buscarPorId(id);
        if (monitoria == null) {
            return "redirect:/monitorias"; // Redireciona se a monitoria não for encontrada
        }

        model.addAttribute("monitoria", monitoria);
        model.addAttribute("usuario", usuario); // Adiciona o usuário autenticado ao modelo
        return "monitoria/visualizarMonitoria";
    }

    @PostMapping("/encerrar/{id}")
    public String encerrarMonitoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.encerrarMonitoria(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Monitoria encerrada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao encerrar monitoria: " + e.getMessage());
        }
        return "redirect:/monitorias"; // Redireciona para a lista de monitorias após o encerramento
    }
}