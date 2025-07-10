package br.com.iftm.monitoria.service;

import br.com.iftm.monitoria.model.Monitoria;
import br.com.iftm.monitoria.model.Presenca;
import br.com.iftm.monitoria.model.dto.MonitoriaRelatorioDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {

    @Autowired
    private MonitoriaService monitoriaService;

    @Autowired
    private PresencaService presencaService;

    public byte[] gerarRelatorioMonitorias() throws Exception {
        // Busca todas as monitorias e presenças
        List<Monitoria> monitorias = monitoriaService.buscarTodos();
        List<Presenca> presencas = presencaService.buscarTodos();

        // Mapeia as monitorias para o DTO de relatório, incluindo as presenças
        List<MonitoriaRelatorioDTO> dados = monitorias.stream()
                .map(m -> new MonitoriaRelatorioDTO(m, presencas))
                .toList();

        // Carrega os arquivos JRXML e compila os relatórios
        InputStream input = new ClassPathResource("reports/monitoria_relatorio.jrxml").getInputStream();
        InputStream subInput = new ClassPathResource("reports/sub_presencas.jrxml").getInputStream();

        JasperReport mainReport = JasperCompileManager.compileReport(input);
        JasperReport subReport = JasperCompileManager.compileReport(subInput);

        // Define o caminho do logo e os parâmetros do relatório
        String logoPath = new ClassPathResource("reports/logo-iftm.png").getFile().getAbsolutePath();

        // Prepara os parâmetros do relatório, incluindo o caminho do logo e o sub-relatório
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("logoPath", logoPath);
        parameters.put("SUBREPORT_PRESENCAS", subReport);

        // Prepara a fonte de dados para o relatório principal e preenche o relatório
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dados);
        JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
