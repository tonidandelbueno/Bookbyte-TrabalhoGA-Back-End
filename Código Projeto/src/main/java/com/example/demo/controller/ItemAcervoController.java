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

import com.example.demo.dto.ItemAcervoAtualizacao;
import com.example.demo.dto.ItemAcervoRequest;
import com.example.demo.model.ItemAcervo;
import com.example.demo.model.StatusLeitura;
import com.example.demo.service.ItemAcervoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/acervo")
@Tag(name = "Acervo", description = "Livros no acervo pessoal de cada usuário, com status e página atual")
public class ItemAcervoController {

    private final ItemAcervoService itemAcervoService;

    public ItemAcervoController(ItemAcervoService itemAcervoService) {
        this.itemAcervoService = itemAcervoService;
    }

    @GetMapping
    @Operation(summary = "Lista o acervo",
            description = "Sem parâmetros, devolve tudo. Pode filtrar por usuarioId e/ou por status (QUERO_LER, LENDO, CONCLUIDO, ABANDONADO).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista devolvida (pode ser vazia)"),
            @ApiResponse(responseCode = "400", description = "Status inválido")
    })
    public List<ItemAcervo> listar(
            @RequestParam(name = "usuarioId", required = false) Long usuarioId,
            @RequestParam(name = "status", required = false) StatusLeitura status) {
        return itemAcervoService.listar(usuarioId, status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um item do acervo pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item encontrado"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    public ItemAcervo buscarPorId(@PathVariable("id") Long id) {
        return itemAcervoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Inclui um livro no acervo de um usuário",
            description = "Se status não for informado, fica QUERO_LER; se paginaAtual não for informada, fica 0.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Livro incluído no acervo"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário ou livro não encontrado"),
            @ApiResponse(responseCode = "409", description = "O livro já está no acervo deste usuário")
    })
    public ItemAcervo criar(@RequestBody ItemAcervoRequest request) {
        return itemAcervoService.criar(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza status e/ou página atual de um item do acervo",
            description = "Só muda o que for enviado; campos ausentes ficam como estavam.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    public ItemAcervo atualizar(@PathVariable("id") Long id, @RequestBody ItemAcervoAtualizacao dados) {
        return itemAcervoService.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove um livro do acervo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item removido"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    public void excluir(@PathVariable("id") Long id) {
        itemAcervoService.excluir(id);
    }
}
