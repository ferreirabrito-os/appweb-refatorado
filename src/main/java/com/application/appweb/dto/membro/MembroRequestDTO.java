package com.application.appweb.dto.membro;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record MembroRequestDTO(
        @NotBlank(message = "O nome é obrigatório") String nome,
        String endereco,
        @NotBlank(message = "O telefone é obrigatório") String telefone,
        @NotBlank(message = "O email é obrigatório") @Email(message = "Formato de email inválido") String email,
        @NotNull(message = "A data de nascimento é obrigatória")
        @Past(message = "A data de nascimento deve estar no passado") LocalDate dataNascimento
) {}
