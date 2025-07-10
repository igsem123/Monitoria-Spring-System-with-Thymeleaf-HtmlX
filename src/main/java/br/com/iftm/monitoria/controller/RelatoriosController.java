package br.com.iftm.monitoria.controller;

import br.com.iftm.monitoria.model.Monitoria;
import br.com.iftm.monitoria.model.dto.MonitoriaRelatorioDTO;
import br.com.iftm.monitoria.service.MonitoriaService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/relatorios")
public class RelatoriosController {

    @Autowired
    private MonitoriaService monitoriaService;

    @GetMapping
    public String listarRelatorios() {
        // Retorna a view de listagem de relatórios
        return "relatorio/lista";
    }

    @GetMapping("/monitorias")
    public ResponseEntity<byte[]> gerarRelatorioMonitorias() {
        // Gera o relatório de monitorias
        try {
            List<Monitoria> monitorias = monitoriaService.buscarTodos(); // ou um filtro

            // Converte para DTO
            List<MonitoriaRelatorioDTO> dados = monitorias.stream()
                    .map(MonitoriaRelatorioDTO::new)
                    .toList();

            // Carrega o .jrxml do resources
            InputStream input = new ClassPathResource("reports/monitoria_relatorio.jrxml").getInputStream();
            JasperReport report = JasperCompileManager.compileReport(input);

            // Cria o DataSource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dados);

            String logoPath = new ClassPathResource("reports/logo-iftm.png").getFile().getAbsolutePath();
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("logoPath", logoPath);

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource);
            byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=monitorias.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
