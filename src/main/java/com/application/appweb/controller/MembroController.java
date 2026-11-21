package com.application.appweb.controller;

import com.application.appweb.dto.membro.*;
import com.application.appweb.service.MembroService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/membros")
public class MembroController {
    private final MembroService service;

    public MembroController(MembroService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MembroResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembroResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<MembroResponseDTO>> findByName(@RequestParam String nome) {
        return ResponseEntity.ok(service.findByName(nome));
    }

    @GetMapping("/idade")
    public ResponseEntity<List<MembroResponseDTO>> findByAge(
            @RequestParam int idadeMinima, @RequestParam int idadeMaxima) {
        return ResponseEntity.ok(service.findByAgeRange(idadeMinima, idadeMaxima));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MembroResponseDTO> create(@Valid @RequestBody MembroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MembroResponseDTO> update(@PathVariable Long id, @Valid @RequestBody MembroRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}





