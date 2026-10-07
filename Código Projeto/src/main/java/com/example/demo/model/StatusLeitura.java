package com.example.demo.model;

// Os quatro estados possíveis de um livro no acervo do leitor.
// É um enum: uma lista fechada de valores (como as opções de um menu).
public enum StatusLeitura {
    QUERO_LER,
    LENDO,
    CONCLUIDO,
    ABANDONADO
}
