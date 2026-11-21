package com.application.appweb.dto.membro;

import java.time.LocalDate;

public record MembroResponseDTO(
        Long id, String nome, String endereco, String telefone, String email, LocalDate dataNascimento
) {}
