package com.application.appweb.dto.financeiro;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ResumoFinanceiroDTO(
        LocalDate dataInicio, LocalDate dataFim,
        BigDecimal totalEntradas, BigDecimal totalSaidas, BigDecimal saldo
) {}
