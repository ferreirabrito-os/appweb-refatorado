package com.application.appweb.dto.financeiro;

import com.application.appweb.model.Financeiro.TipoRegistro;
import java.math.BigDecimal;
import java.time.LocalDate;

public record FinanceiroResponseDTO(
        Long id, String descricao, BigDecimal valor, TipoRegistro tipoRegistro,
        LocalDate dataRegistro, Long membroId, String membroNome
) {}
