package com.application.appweb.controller;

import com.application.appweb.dto.financeiro.FinanceiroResponseDTO;
import com.application.appweb.service.*;
import com.lowagie.text.DocumentException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/financeiros/pdf")
public class FinanceiroPdfController {
    private final FinanceiroService financeiroService;
    private final PdfService pdfService;

    public FinanceiroPdfController(FinanceiroService financeiroService, PdfService pdfService) {
        this.financeiroService = financeiroService;
        this.pdfService = pdfService;
    }

    @GetMapping("/financeiro")
    public ResponseEntity<byte[]> geral(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim)
            throws DocumentException {
        return pdf("Relatório Financeiro Geral", dataInicio, dataFim, "relatorio-financeiro.pdf");
    }

    @GetMapping("/entradas")
    public ResponseEntity<byte[]> entradas(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim)
            throws DocumentException {
        return pdf("Relatório de Entradas Financeiras", dataInicio, dataFim, "relatorio-entradas.pdf");
    }

    @GetMapping("/saidas")
    public ResponseEntity<byte[]> saidas(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim)
            throws DocumentException {
        return pdf("Relatório de Saídas Financeiras", dataInicio, dataFim, "relatorio-saidas.pdf");
    }

    private ResponseEntity<byte[]> pdf(String titulo, LocalDate inicio, LocalDate fim, String nome)
            throws DocumentException {
        List<FinanceiroResponseDTO> registros = financeiroService.findByPeriod(inicio, fim);
        BigDecimal entradas = financeiroService.totalEntradas(inicio, fim);
        BigDecimal saidas = financeiroService.totalSaidas(inicio, fim);

        byte[] bytes = pdfService.gerarRelatorio(titulo, registros, entradas, saidas, inicio, fim);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename(nome).build());
        headers.setContentLength(bytes.length);
        return ResponseEntity.ok().headers(headers).body(bytes);
    }
}
