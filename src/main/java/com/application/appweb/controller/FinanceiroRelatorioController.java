package com.application.appweb.controller;

import com.application.appweb.dto.financeiro.ResumoFinanceiroDTO;
import com.application.appweb.service.FinanceiroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/financeiros/relatorios")
public class FinanceiroRelatorioController {
    private final FinanceiroService service;

    public FinanceiroRelatorioController(FinanceiroService service) {
        this.service = service;
    }

    @GetMapping("/saldo")
    public ResponseEntity<BigDecimal> saldo(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
        return ResponseEntity.ok(service.resumo(dataInicio, dataFim).saldo());
    }

    @GetMapping("/entradas")
    public ResponseEntity<BigDecimal> entradas(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
        return ResponseEntity.ok(service.totalEntradas(dataInicio, dataFim));
    }

    @GetMapping("/saidas")
    public ResponseEntity<BigDecimal> saidas(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
        return ResponseEntity.ok(service.totalSaidas(dataInicio, dataFim));
    }

    @GetMapping("/resumo")
    public ResponseEntity<ResumoFinanceiroDTO> resumo(
            @RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
        return ResponseEntity.ok(service.resumo(dataInicio, dataFim));
    }
}
