package br.com.iftm.monitoria.controller;

import br.com.iftm.monitoria.model.Monitoria;
import br.com.iftm.monitoria.model.Presenca;
import br.com.iftm.monitoria.model.dto.MonitoriaRelatorioDTO;
import br.com.iftm.monitoria.service.MonitoriaService;
import br.com.iftm.monitoria.service.PresencaService;
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

    @Autowired
    private PresencaService presencaService;

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
            List<Presenca> presencas = presencaService.buscarTodos();

            // Converte para DTO
            List<MonitoriaRelatorioDTO> dados = monitorias.stream()
                    .map(m -> new MonitoriaRelatorioDTO(m, presencas))
                    .toList();

            // Compila os relatórios .jrxml
            InputStream input = new ClassPathResource("reports/monitoria_relatorio.jrxml").getInputStream();
            InputStream subInput = new ClassPathResource("reports/sub_presencas.jrxml").getInputStream();

            JasperReport mainReport = JasperCompileManager.compileReport(input);
            JasperReport subReport = JasperCompileManager.compileReport(subInput);

            // Parâmetros
            String logoPath = new ClassPathResource("reports/logo-iftm.png").getFile().getAbsolutePath();
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("logoPath", logoPath);
            parameters.put("SUBREPORT_PRESENCAS", subReport);

            // Cria o DataSource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dados);
            JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, dataSource);
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
