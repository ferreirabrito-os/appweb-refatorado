package com.application.appweb.dto.financeiro;

import com.application.appweb.model.Financeiro.TipoRegistro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record FinanceiroRequestDTO(
        @NotBlank(message = "A descrição é obrigatória") String descricao,
        @NotNull(message = "O valor é obrigatório") @Positive(message = "O valor deve ser maior que zero") BigDecimal valor,
        @NotNull(message = "O tipo de registro é obrigatório") TipoRegistro tipoRegistro,
        @NotNull(message = "A data de registro é obrigatória") LocalDate dataRegistro,
        Long membroId
) {}
