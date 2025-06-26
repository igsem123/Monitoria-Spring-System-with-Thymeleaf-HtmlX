package br.com.iftm.monitoria.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    @GetMapping("/layout")
    public String layout(Model model) {
        model.addAttribute("conteudo", "layout");
        model.addAttribute("pageTitle", "Monitoria IFTM");
        return "layout";
    }

    @GetMapping("/error")
    public String lidarComErro(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String url = String.valueOf(request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
        String fragment = "fragments/error/default";

        if (status != null) {
            HttpStatus httpStatus = HttpStatus.resolve((int) status);
            if (httpStatus == HttpStatus.NOT_FOUND) {
                LOGGER.warn("A URL {} foi acessada mas não existe.", url);
                fragment = "fragments/error/404";
            } else if (httpStatus == HttpStatus.FORBIDDEN) {
                LOGGER.warn("Tentaram acessar a URL {} sem permissão.", url);
                fragment = "fragments/error/403";
            } else if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR) {
                LOGGER.error("Ocorreu um erro interno no servidor.");
                fragment = "fragments/error/500";
            }
        }

        model.addAttribute("conteudo", fragment);
        model.addAttribute("pageTitle", "Erro no Sistema");

        boolean htmxRequest = "true".equals(request.getHeader("HX-Request"));

        // Se for uma requisição HTMX, retorne apenas o fragmento para o HTMX injetar.
        if (htmxRequest) return fragment; else return "layout";
    }
}
