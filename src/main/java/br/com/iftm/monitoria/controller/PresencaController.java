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

import java.security.Principal;
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
            Principal principal
    ) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        Usuario monitor = usuarioService.buscarPorEmail(email);

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<Presenca> presencasPage = presencaService.listarTodosPaginado(PageRequest.of(currentPage - 1, pageSize), monitor.getId());
        model.addAttribute("presencasPage", presencasPage);

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
        return  "fragments/presenca/modal-criacao-presenca :: modalCriacaoPresenca";
    }

    @HxRequest
    @PostMapping
    public String registrarPresenca(
            @RequestParam("monitoriaId") Long monitoriaId,
            Model model,
            Principal principal
    ) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        Usuario monitor = usuarioService.buscarPorEmail(email);

        Monitoria monitoria = monitoriaService.buscarPorId(monitoriaId);
        if (monitoria != null && monitoria.getMonitor().getId().equals(monitor.getId())) {
            Presenca presenca = new Presenca();
            presenca.setMonitoria(monitoria);
            presencaService.salvar(presenca);
            model.addAttribute("successMessage", "Presença registrada com sucesso!");
        } else {
            model.addAttribute("errorMessage", "Monitoria não encontrada ou você não tem permissão para registrar presença.");
        }

        return "redirect:/presenca";
    }

    @DeleteMapping("/excluir")
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
}
