package com.example.demo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.Livro;
import com.example.demo.repository.ItemAcervoRepository;
import com.example.demo.repository.LivroRepository;

// O Service guarda as regras de negócio. O Controller só recebe a requisição
// e chama o Service; o Service decide o que pode ou não pode e usa o Repository.
@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final ItemAcervoRepository itemAcervoRepository;

    // O Spring entrega os repositórios prontos aqui (injeção pelo construtor).
    public LivroService(LivroRepository livroRepository, ItemAcervoRepository itemAcervoRepository) {
        this.livroRepository = livroRepository;
        this.itemAcervoRepository = itemAcervoRepository;
    }

    // GET /livros e GET /livros?titulo=...
    public List<Livro> listar(String titulo) {
        if (titulo != null && !titulo.isBlank()) {
            return livroRepository.findByTituloContainingIgnoreCase(titulo);
        }
        return livroRepository.findAll();
    }

    // GET /livros/{id}
    public Livro buscarPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Livro não encontrado: " + id));
    }

    // POST /livros
    public Livro criar(Livro livro) {
        validar(livro);
        livro.setId(null); // garante que é um livro novo, mesmo que o JSON traga um id
        return livroRepository.save(livro);
    }

    // PUT /livros/{id}
    public Livro atualizar(Long id, Livro dados) {
        Livro livro = buscarPorId(id);
        validar(dados);
        livro.setIsbn(dados.getIsbn());
        livro.setTitulo(dados.getTitulo());
        livro.setAutor(dados.getAutor());
        livro.setEditora(dados.getEditora());
        livro.setIdioma(dados.getIdioma());
        livro.setPaginas(dados.getPaginas());
        livro.setDataPublicacao(dados.getDataPublicacao());
        livro.setCapa(dados.getCapa());
        return livroRepository.save(livro);
    }

    // DELETE /livros/{id}
    public void excluir(Long id) {
        Livro livro = buscarPorId(id);
        if (itemAcervoRepository.existsByLivroId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível excluir: o livro está no acervo de algum usuário");
        }
        livroRepository.delete(livro);
    }

    private void validar(Livro livro) {
        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O título é obrigatório");
        }
        if (livro.getAutor() == null || livro.getAutor().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O autor é obrigatório");
        }
        if (livro.getPaginas() != null && livro.getPaginas() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O número de páginas deve ser maior que zero");
        }
    }
}
