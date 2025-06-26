package br.com.iftm.monitoria.controller;

import br.com.iftm.monitoria.model.Monitoria;
import br.com.iftm.monitoria.service.MonitoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/monitorias")
public class MonitoriaController {

    @Autowired
    private MonitoriaService service;

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
        return "listaMonitorias"; // Nome da View HTML para a listagem de monitorias
    }
}