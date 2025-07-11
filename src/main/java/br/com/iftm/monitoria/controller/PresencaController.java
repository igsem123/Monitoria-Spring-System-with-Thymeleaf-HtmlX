package br.com.iftm.monitoria.controller;

import br.com.iftm.monitoria.model.Monitoria;
import br.com.iftm.monitoria.model.Presenca;
import br.com.iftm.monitoria.model.Usuario;
import br.com.iftm.monitoria.service.MonitoriaService;
import br.com.iftm.monitoria.service.PresencaService;
import br.com.iftm.monitoria.service.UsuarioService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/presenca")
public class PresencaController {

    @Autowired
    PresencaService presencaService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    MonitoriaService monitoriaService;

    @GetMapping
    public String listarPresencas(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            Principal principal,
            @ModelAttribute("successMessage") String successMessage,
            @ModelAttribute("errorMessage") String errorMessage
    ) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        Usuario monitor = usuarioService.buscarPorEmail(email);

        int currentPage = page.orElse(0);
        int pageSize = size.orElse(10);

        Page<Presenca> presencasPage = presencaService.listarTodosPaginado(PageRequest.of(currentPage, pageSize), monitor.getId());
        model.addAttribute("presencasPage", presencasPage);
        model.addAttribute("successMessage", successMessage);
        model.addAttribute("errorMessage", errorMessage);

        int totalPages = presencasPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();

            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "/presenca/listaPresencas";
    }

    @HxRequest
    @GetMapping("/cadastrar")
    public String cadastrarPresenca(
            Model model,
            Principal principal
    ) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        Usuario monitor = usuarioService.buscarPorEmail(email);
        model.addAttribute("monitorias", monitoriaService.buscarMonitoriasDoMonitorAtivas(monitor));
        return "fragments/presenca/modal-criacao-presenca :: modalCriacaoPresenca";
    }

    @PostMapping
    public String registrarPresenca(
            @RequestParam("monitoriaId") Long monitoriaId,
            @RequestParam("dataPresenca") String dataPresenca,
            @RequestParam("qtdAlunosPresentes") Integer qtdAlunosPresentes,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        Usuario monitor = usuarioService.buscarPorEmail(email);

        Monitoria monitoria = monitoriaService.buscarPorId(monitoriaId);
        if (monitoria != null && monitoria.getMonitor().getId().equals(monitor.getId())) {
            try {
                Presenca presenca = new Presenca();
                presenca.setMonitoria(monitoria);

                // Converto a data para LocalDate
                LocalDate data = LocalDate.parse(dataPresenca);
                presenca.setData(data);

                presenca.setQtdAlunosPresentes(qtdAlunosPresentes);
                presencaService.salvar(presenca);
                redirectAttributes.addFlashAttribute("successMessage", "Presença registrada com sucesso!");
            } catch (IllegalArgumentException ex) {
                redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            }

        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Monitoria não encontrada ou você não tem permissão para registrar presença.");
        }

        return "redirect:/presenca";
    }

    @PostMapping("/excluir")
    public String excluirPresenca(
            @RequestParam("presencaId") Long presencaId,
            Model model
    ) {
        if (presencaService.excluir(presencaId)) {
            model.addAttribute("successMessage", "Presença deletada com sucesso!");
        } else {
            model.addAttribute("errorMessage", "Erro ao deletar a presença.");
        }

        return "redirect:/presenca";
    }

    @HxRequest
    @GetMapping("/editar/{id}")
    public String editarPresenca(
            @PathVariable("id") Long id,
            Model model,
            Principal principal
    ) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        Usuario monitor = usuarioService.buscarPorEmail(email);

        Optional<Presenca> presenca = presencaService.buscarPorId(id);
        if (presenca.isPresent()) {
            model.addAttribute("presenca", presenca.get());
            model.addAttribute("monitorias", monitoriaService.buscarMonitoriasDoMonitorAtivas(monitor));
            return "fragments/presenca/modal-edicao-presenca :: modalEdicaoPresenca";
        } else {
            model.addAttribute("errorMessage", "Monitoria não encontrada ou você não tem permissão para editar presença.");
            return "redirect:/presenca";
        }
    }

    @PostMapping("/atualizar")
    public String atualizarPresenca(
            @RequestParam("id") Long id,
            @RequestParam("monitoriaId") Long monitoriaId,
            @RequestParam("dataPresenca") String dataPresenca,
            @RequestParam("qtdAlunosPresentes") Integer qtdAlunosPresentes,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        Usuario monitor = usuarioService.buscarPorEmail(email);

        Optional<Presenca> optionalPresenca = presencaService.buscarPorId(id);

        if (optionalPresenca.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Presença não encontrada.");
            return "redirect:/presenca";
        }

        Presenca presenca = optionalPresenca.get();

        Monitoria monitoria = monitoriaService.buscarPorId(monitoriaId);
        if (monitoria != null && monitoria.getMonitor().getId().equals(monitor.getId())) {
            presenca.setMonitoria(monitoria);
            presenca.setData(LocalDate.parse(dataPresenca));
            presenca.setQtdAlunosPresentes(qtdAlunosPresentes);
            presencaService.salvar(presenca);
            redirectAttributes.addFlashAttribute("successMessage", "Presença atualizada com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Monitoria não encontrada ou acesso negado.");
        }

        return "redirect:/presenca";
    }
}
