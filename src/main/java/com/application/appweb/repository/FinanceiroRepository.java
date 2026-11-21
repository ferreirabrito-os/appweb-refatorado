package com.application.appweb.repository;

import com.application.appweb.model.Financeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FinanceiroRepository extends JpaRepository<Financeiro, Long> {
    List<Financeiro> findByDataRegistroBetween(LocalDate inicio, LocalDate fim);
    List<Financeiro> findByTipoRegistroAndDataRegistroBetween(
            Financeiro.TipoRegistro tipo, LocalDate inicio, LocalDate fim);
    List<Financeiro> findByMembroId(Long membroId);
    List<Financeiro> findByMembroNomeContainingIgnoreCase(String nome);

    @Query("""
        select coalesce(sum(f.valor), 0)
        from Financeiro f
        where f.tipoRegistro = :tipo
          and f.dataRegistro between :inicio and :fim
    """)
    BigDecimal sumByTipoAndPeriodo(
            @Param("tipo") Financeiro.TipoRegistro tipo,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );
}
