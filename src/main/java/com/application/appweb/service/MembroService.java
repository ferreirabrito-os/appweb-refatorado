package com.application.appweb.service;

import com.application.appweb.dto.membro.*;
import com.application.appweb.exception.ApiException;
import com.application.appweb.model.Membro;
import com.application.appweb.repository.MembroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class MembroService {
    private final MembroRepository repository;

    public MembroService(MembroRepository repository) {
        this.repository = repository;
    }

    public List<MembroResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public MembroResponseDTO findById(Long id) {
        return toResponse(getEntity(id));
    }

    @Transactional
    public MembroResponseDTO create(MembroRequestDTO dto) {
        Membro m = new Membro();
        copy(dto, m);
        return toResponse(repository.save(m));
    }

    @Transactional
    public MembroResponseDTO update(Long id, MembroRequestDTO dto) {
        Membro m = getEntity(id);
        copy(dto, m);
        return toResponse(repository.save(m));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Membro não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    public List<MembroResponseDTO> findByName(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "O nome para busca é obrigatório");
        }
        return repository.findByNomeContainingIgnoreCase(nome.trim()).stream().map(this::toResponse).toList();
    }

    public List<MembroResponseDTO> findByAgeRange(int min, int max) {
        if (min < 0 || max < 0 || min > max) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Intervalo de idade inválido");
        }
        LocalDate today = LocalDate.now();
        LocalDate oldest = today.minusYears(max);
        LocalDate youngest = today.minusYears(min);
        return repository.findByDataNascimentoBetween(oldest, youngest).stream().map(this::toResponse).toList();
    }

    private Membro getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Membro não encontrado: " + id));
    }

    private void copy(MembroRequestDTO dto, Membro m) {
        m.setNome(dto.nome().trim());
        m.setEndereco(dto.endereco() == null ? null : dto.endereco().trim());
        m.setTelefone(dto.telefone().trim());
        m.setEmail(dto.email().trim().toLowerCase());
        m.setDataNascimento(dto.dataNascimento());
    }

    private MembroResponseDTO toResponse(Membro m) {
        return new MembroResponseDTO(m.getId(), m.getNome(), m.getEndereco(),
                m.getTelefone(), m.getEmail(), m.getDataNascimento());
    }
}
