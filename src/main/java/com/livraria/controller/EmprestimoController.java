package com.livraria.controller;

import com.livraria.service.EmprestimoSerivce;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/emprestimos")
@RestController
public class EmprestimoController {

    private final EmprestimoSerivce service;



    @PostMapping("/realizar/{usuarioId}/{tipoItem}/{itemId}")
    public String realizar(@PathVariable Long usuarioId, @PathVariable String tipoItem, @PathVariable Long itemId) {
        return service.realizarEmprestimo(usuarioId, tipoItem, itemId);
    }

    @PostMapping("/devolver/{id}")
    public String devolver(@PathVariable Long id) {
        return service.devolverItem(id);
    }
}
