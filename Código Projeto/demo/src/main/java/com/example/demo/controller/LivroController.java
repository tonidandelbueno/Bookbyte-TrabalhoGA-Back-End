package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Livro;
import com.example.demo.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

// O Controller é a "porta de entrada": recebe a requisição HTTP, chama o Service
// e devolve a resposta em JSON. As anotações @Operation/@ApiResponse/@Tag
// só alimentam a documentação do Swagger UI.

@RestController
@RequestMapping("/livros")
@Tag(name = "Livros", description = "Catálogo de livros do BookByte")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    @Operation(summary = "Lista os livros",
            description = "Devolve todos os livros. Com o parâmetro titulo, devolve só os que contêm o texto informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista devolvida (pode ser vazia)")
    })
    public List<Livro> listar(@RequestParam(name = "titulo", required = false) String titulo) {
        return livroService.listar(titulo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um livro pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro encontrado"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public Livro buscarPorId(@PathVariable("id") Long id) {
        return livroService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra um livro", description = "Título e autor são obrigatórios.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Livro criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Livro criar(@RequestBody Livro livro) {
        return livroService.criar(livro);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um livro", description = "Substitui os dados do livro pelos enviados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public Livro atualizar(@PathVariable("id") Long id, @RequestBody Livro livro) {
        return livroService.atualizar(id, livro);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui um livro",
            description = "Não permite excluir um livro que está no acervo de algum usuário.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livro excluído"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "409", description = "Livro está em uso em algum acervo")
    })
    public void excluir(@PathVariable("id") Long id) {
        livroService.excluir(id);
    }
}
