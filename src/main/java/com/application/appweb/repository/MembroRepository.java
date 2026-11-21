package com.application.appweb.repository;

import com.application.appweb.model.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface MembroRepository extends JpaRepository<Membro, Long> {
    List<Membro> findByDataNascimentoBetween(LocalDate inicio, LocalDate fim);
    List<Membro> findByNomeContainingIgnoreCase(String nome);
}
