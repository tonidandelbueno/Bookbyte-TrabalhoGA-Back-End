package com.example.demo.dto;

import com.example.demo.model.StatusLeitura;

// Corpo do PUT /acervo/{id}. Só o que pode mudar: status e/ou página atual.
// O que vier null não é alterado.
public record ItemAcervoAtualizacao(StatusLeitura status, Integer paginaAtual) {
}
