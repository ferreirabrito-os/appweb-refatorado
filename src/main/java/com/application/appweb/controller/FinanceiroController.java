package com.application.appweb.controller;

import com.application.appweb.dto.financeiro.*;
import com.application.appweb.model.Financeiro.TipoRegistro;
import com.application.appweb.service.FinanceiroService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/financeiros")
public class FinanceiroController {
    private final FinanceiroService service;

    public FinanceiroController(FinanceiroService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinanceiroResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/consulta/membro/{id}")
    public ResponseEntity<List<FinanceiroResponseDTO>> byMember(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByMember(id));
    }

    @GetMapping("/consulta/membro")
    public ResponseEntity<List<FinanceiroResponseDTO>> byMemberName(@RequestParam String nome) {
        return ResponseEntity.ok(service.findByMemberName(nome));
    }

    @GetMapping("/consulta/periodo")
    public ResponseEntity<List<FinanceiroResponseDTO>> byPeriod(
            @RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
        return ResponseEntity.ok(service.findByPeriod(dataInicio, dataFim));
    }

    @GetMapping("/consulta/entradas")
    public ResponseEntity<List<FinanceiroResponseDTO>> entradas(
            @RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
        return ResponseEntity.ok(service.findByType(TipoRegistro.ENTRADA, dataInicio, dataFim));
    }

    @GetMapping("/consulta/saidas")
    public ResponseEntity<List<FinanceiroResponseDTO>> saidas(
            @RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
        return ResponseEntity.ok(service.findByType(TipoRegistro.SAIDA, dataInicio, dataFim));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FinanceiroResponseDTO> create(@Valid @RequestBody FinanceiroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FinanceiroResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody FinanceiroRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
