package com.example.demo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.ItemAcervoAtualizacao;
import com.example.demo.dto.ItemAcervoRequest;
import com.example.demo.model.ItemAcervo;
import com.example.demo.model.Livro;
import com.example.demo.model.StatusLeitura;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ItemAcervoRepository;

// Regras do acervo: um livro entra no acervo de um usuário, com status e página atual.
@Service
public class ItemAcervoService {

    private final ItemAcervoRepository itemAcervoRepository;
    private final UsuarioService usuarioService;
    private final LivroService livroService;

    public ItemAcervoService(ItemAcervoRepository itemAcervoRepository,
                             UsuarioService usuarioService,
                             LivroService livroService) {
        this.itemAcervoRepository = itemAcervoRepository;
        this.usuarioService = usuarioService;
        this.livroService = livroService;
    }

    // GET /acervo, com filtros opcionais por usuário e/ou status
    public List<ItemAcervo> listar(Long usuarioId, StatusLeitura status) {
        if (usuarioId != null && status != null) {
            return itemAcervoRepository.findByUsuarioIdAndStatus(usuarioId, status);
        }
        if (usuarioId != null) {
            return itemAcervoRepository.findByUsuarioId(usuarioId);
        }
        if (status != null) {
            return itemAcervoRepository.findByStatus(status);
        }
        return itemAcervoRepository.findAll();
    }

    // GET /acervo/{id}
    public ItemAcervo buscarPorId(Long id) {
        return itemAcervoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Item do acervo não encontrado: " + id));
    }

    // POST /acervo
    public ItemAcervo criar(ItemAcervoRequest request) {
        if (request.usuarioId() == null || request.livroId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "usuarioId e livroId são obrigatórios");
        }

        // Se o usuário ou o livro não existirem, esses métodos já respondem 404.
        Usuario usuario = usuarioService.buscarPorId(request.usuarioId());
        Livro livro = livroService.buscarPorId(request.livroId());

        if (itemAcervoRepository.existsByUsuarioIdAndLivroId(usuario.getId(), livro.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Este livro já está no acervo do usuário");
        }

        // Valores padrão quando o cliente não informa (como diz o comentário do ItemAcervoRequest).
        StatusLeitura status = request.status() != null ? request.status() : StatusLeitura.QUERO_LER;
        int paginaAtual = request.paginaAtual() != null ? request.paginaAtual() : 0;
        validarPagina(paginaAtual, livro);

        ItemAcervo item = new ItemAcervo(usuario, livro, status, paginaAtual);
        return itemAcervoRepository.save(item);
    }

    // PUT /acervo/{id}: o que vier null no JSON não é alterado
    public ItemAcervo atualizar(Long id, ItemAcervoAtualizacao dados) {
        ItemAcervo item = buscarPorId(id);

        if (dados.status() != null) {
            item.setStatus(dados.status());
        }
        if (dados.paginaAtual() != null) {
            validarPagina(dados.paginaAtual(), item.getLivro());
            item.setPaginaAtual(dados.paginaAtual());
        }
        return itemAcervoRepository.save(item);
    }

    // DELETE /acervo/{id}: tira o livro do acervo
    public void excluir(Long id) {
        ItemAcervo item = buscarPorId(id);
        itemAcervoRepository.delete(item);
    }

    private void validarPagina(int paginaAtual, Livro livro) {
        if (paginaAtual < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "paginaAtual não pode ser negativa");
        }
        if (livro.getPaginas() != null && paginaAtual > livro.getPaginas()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "paginaAtual não pode ser maior que o total de páginas do livro ("
                            + livro.getPaginas() + ")");
        }
    }
}
