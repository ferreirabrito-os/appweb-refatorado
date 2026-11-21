package com.application.appweb.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "membros")
public class Membro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 250)
    private String endereco;

    @Column(nullable = false, length = 30)
    private String telefone;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @OneToMany(mappedBy = "membro", fetch = FetchType.LAZY)
    private List<Financeiro> registrosFinanceiros = new ArrayList<>();

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public List<Financeiro> getRegistrosFinanceiros() { return registrosFinanceiros; }

    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setEmail(String email) { this.email = email; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public void setRegistrosFinanceiros(List<Financeiro> registrosFinanceiros) { this.registrosFinanceiros = registrosFinanceiros; }
}
