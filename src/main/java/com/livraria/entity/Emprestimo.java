package com.livraria.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataEmprestimo = LocalDate.now();
    private LocalDate dataDevolucao;
    private LocalDate dataLimite;

    private BigDecimal multaGerada = BigDecimal.ZERO;

    @ManyToOne
    private Usuario usuario;

    private String tipoItem;
    private Long itemId;
}
