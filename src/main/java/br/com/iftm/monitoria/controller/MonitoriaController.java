package br.com.iftm.monitoria.controller;

import br.com.iftm.monitoria.model.Monitoria;
import br.com.iftm.monitoria.service.MonitoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/monitorias")
public class MonitoriaController {

    @Autowired
    private MonitoriaService service;

    @GetMapping
    public String listarMonitorias(Model model) {
        List<Monitoria> monitorias = service.listarTodos();
        model.addAttribute("monitorias", monitorias);
        return "monitorias";
    }
}