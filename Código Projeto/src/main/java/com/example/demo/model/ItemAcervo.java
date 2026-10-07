package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

// Um livro dentro do acervo de um usuário, com o status e o progresso de leitura.
// Ligação: muitos ItemAcervo para um Usuario, e muitos ItemAcervo para um Livro.
@Entity
public class ItemAcervo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    private Livro livro;

    @Enumerated(EnumType.STRING)
    private StatusLeitura status;

    private Integer paginaAtual;

    public ItemAcervo() {
    }

    public ItemAcervo(Usuario usuario, Livro livro, StatusLeitura status, Integer paginaAtual) {
        this.usuario = usuario;
        this.livro = livro;
        this.status = status;
        this.paginaAtual = paginaAtual;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public StatusLeitura getStatus() {
        return status;
    }

    public void setStatus(StatusLeitura status) {
        this.status = status;
    }

    public Integer getPaginaAtual() {
        return paginaAtual;
    }

    public void setPaginaAtual(Integer paginaAtual) {
        this.paginaAtual = paginaAtual;
    }

}
