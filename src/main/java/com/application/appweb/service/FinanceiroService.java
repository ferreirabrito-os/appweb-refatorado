package com.application.appweb.service;

import com.application.appweb.dto.financeiro.*;
import com.application.appweb.exception.ApiException;
import com.application.appweb.model.Financeiro;
import com.application.appweb.model.Membro;
import com.application.appweb.repository.FinanceiroRepository;
import com.application.appweb.repository.MembroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class FinanceiroService {
    private final FinanceiroRepository financeiroRepository;
    private final MembroRepository membroRepository;

    public FinanceiroService(FinanceiroRepository financeiroRepository, MembroRepository membroRepository) {
        this.financeiroRepository = financeiroRepository;
        this.membroRepository = membroRepository;
    }

    @Transactional
    public FinanceiroResponseDTO create(FinanceiroRequestDTO dto) {
        Financeiro f = new Financeiro();
        copy(dto, f);
        return toResponse(financeiroRepository.save(f));
    }

    @Transactional
    public FinanceiroResponseDTO update(Long id, FinanceiroRequestDTO dto) {
        Financeiro f = getEntity(id);
        copy(dto, f);
        return toResponse(financeiroRepository.save(f));
    }

    @Transactional
    public void delete(Long id) {
        if (!financeiroRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Registro financeiro não encontrado: " + id);
        }
        financeiroRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public FinanceiroResponseDTO findById(Long id) {
        return toResponse(getEntity(id));
    }

    @Transactional(readOnly = true)
    public List<FinanceiroResponseDTO> findByPeriod(LocalDate inicio, LocalDate fim) {
        validatePeriod(inicio, fim);
        return financeiroRepository.findByDataRegistroBetween(inicio, fim).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<FinanceiroResponseDTO> findByType(Financeiro.TipoRegistro tipo, LocalDate inicio, LocalDate fim) {
        validatePeriod(inicio, fim);
        return financeiroRepository.findByTipoRegistroAndDataRegistroBetween(tipo, inicio, fim)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<FinanceiroResponseDTO> findByMember(Long membroId) {
        return financeiroRepository.findByMembroId(membroId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<FinanceiroResponseDTO> findByMemberName(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "O nome do membro é obrigatório");
        }
        return financeiroRepository.findByMembroNomeContainingIgnoreCase(nome.trim()).stream()
                .map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public BigDecimal totalEntradas(LocalDate inicio, LocalDate fim) {
        validatePeriod(inicio, fim);
        return zero(financeiroRepository.sumByTipoAndPeriodo(Financeiro.TipoRegistro.ENTRADA, inicio, fim));
    }

    @Transactional(readOnly = true)
    public BigDecimal totalSaidas(LocalDate inicio, LocalDate fim) {
        validatePeriod(inicio, fim);
        return zero(financeiroRepository.sumByTipoAndPeriodo(Financeiro.TipoRegistro.SAIDA, inicio, fim));
    }

    @Transactional(readOnly = true)
    public ResumoFinanceiroDTO resumo(LocalDate inicio, LocalDate fim) {
        BigDecimal entradas = totalEntradas(inicio, fim);
        BigDecimal saidas = totalSaidas(inicio, fim);
        return new ResumoFinanceiroDTO(inicio, fim, entradas, saidas, entradas.subtract(saidas));
    }

    private Financeiro getEntity(Long id) {
        return financeiroRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Registro financeiro não encontrado: " + id));
    }

    private void copy(FinanceiroRequestDTO dto, Financeiro f) {
        f.setDescricao(dto.descricao().trim());
        f.setValor(dto.valor());
        f.setTipoRegistro(dto.tipoRegistro());
        f.setDataRegistro(dto.dataRegistro());

        if (dto.membroId() == null) {
            f.setMembro(null);
        } else {
            Membro membro = membroRepository.findById(dto.membroId())
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Membro não encontrado: " + dto.membroId()));
            f.setMembro(membro);
        }
    }

    private FinanceiroResponseDTO toResponse(Financeiro f) {
        Membro m = f.getMembro();
        return new FinanceiroResponseDTO(
                f.getId(), f.getDescricao(), f.getValor(), f.getTipoRegistro(),
                f.getDataRegistro(), m == null ? null : m.getId(), m == null ? null : m.getNome()
        );
    }

    private BigDecimal zero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private void validatePeriod(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null || inicio.isAfter(fim)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Período inválido: dataInicio deve ser menor ou igual a dataFim");
        }
    }
}
