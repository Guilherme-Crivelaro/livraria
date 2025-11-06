package com.livraria.repository;

import com.livraria.entity.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
}
