package com.example.demo.dto;

import com.example.demo.model.StatusLeitura;

// Corpo do POST /acervo. Um "record" é uma classe só de dados (parecida com um dataclass do Python).
// status e paginaAtual são opcionais: se faltarem, o service usa QUERO_LER e página 0.
public record ItemAcervoRequest(Long usuarioId, Long livroId, StatusLeitura status, Integer paginaAtual) {
}
