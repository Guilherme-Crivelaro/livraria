package com.livraria.service;

import com.livraria.entity.Book;
import com.livraria.entity.Emprestimo;
import com.livraria.entity.Periodico;
import com.livraria.entity.Usuario;
import com.livraria.repository.BookRepository;
import com.livraria.repository.EmprestimoRepository;
import com.livraria.repository.PeriodicoRepository;
import com.livraria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmprestimoSerivce {
    private final EmprestimoRepository emprestimoRepo;
    private final UsuarioRepository usuarioRepo;
    private final BookRepository bookRepo;
    private final PeriodicoRepository periodicoRepo;

    public String realizarEmprestimo(Long usuarioId, String tipoItem, Long itemId) {
        Usuario usuario = usuarioRepo.findById(usuarioId).orElseThrow();
        List<Emprestimo> ativos = emprestimoRepo.findAll()
                .stream().filter(e -> e.getUsuario().getId().equals(usuarioId) && e.getDataDevolucao() == null).toList();


        boolean possuiAtraso = ativos.stream().anyMatch(e -> e.getDataLimite().isBefore(LocalDate.now()));
        if (usuario.getMultaPendente().compareTo(BigDecimal.ZERO) > 0) return "Usuário possui multa pendente!";
        if (ativos.size() >= usuario.getLimiteEmprestimos()) return "Limite de empréstimos atingido!";
        if (possuiAtraso) return "Usuário possui item em atraso!";

        if (tipoItem.equalsIgnoreCase("Livro")) {
            Book b = bookRepo.findById(itemId).orElseThrow();
            if (!b.isAvailable()) return "Livro não disponível!";
            b.setAvailable(false);
            bookRepo.save(b);
        } else {
            Periodico p = periodicoRepo.findById(itemId).orElseThrow();
            if (!p.isAvailable()) return "Periódico não disponível!";
            p.setAvailable(false);
            periodicoRepo.save(p);
        }

        Emprestimo emp = new Emprestimo();
        emp.setUsuario(usuario);
        emp.setTipoItem(tipoItem);
        emp.setItemId(itemId);
        emp.setDataLimite(LocalDate.now().plusDays(7));
        emprestimoRepo.save(emp);

        return "Empréstimo realizado com sucesso!";
    }

    public String devolverItem(Long emprestimoId) {
        Emprestimo e = emprestimoRepo.findById(emprestimoId).orElseThrow();
        e.setDataDevolucao(LocalDate.now());
        if (e.getDataLimite().isBefore(LocalDate.now())) {
            long diasAtraso = ChronoUnit.DAYS.between(e.getDataLimite(), LocalDate.now());
            BigDecimal multa = BigDecimal.valueOf(diasAtraso).multiply(BigDecimal.valueOf(2));
            e.setMultaGerada(multa);
            Usuario u = e.getUsuario();
            u.setMultaPendente(u.getMultaPendente().add(multa));
            usuarioRepo.save(u);
        }
        if (e.getTipoItem().equalsIgnoreCase("Livro")) {
            Book b = bookRepo.findById(e.getItemId()).orElseThrow();
            b.setAvailable(true);
            bookRepo.save(b);
        } else {
            Periodico p = periodicoRepo.findById(e.getItemId()).orElseThrow();
            p.setAvailable(true);
            periodicoRepo.save(p);
        }
        emprestimoRepo.save(e);
        return "Item devolvido!";
    }
}

