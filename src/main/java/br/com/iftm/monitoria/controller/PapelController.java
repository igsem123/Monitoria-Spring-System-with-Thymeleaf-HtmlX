package br.com.iftm.monitoria.controller;

import br.com.iftm.monitoria.service.PapelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/papeis")
public class PapelController {

    @Autowired
    private PapelService service;
}
