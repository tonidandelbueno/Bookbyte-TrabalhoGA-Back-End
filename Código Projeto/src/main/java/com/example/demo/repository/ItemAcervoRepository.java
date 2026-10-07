package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ItemAcervo;
import com.example.demo.model.StatusLeitura;

public interface ItemAcervoRepository extends JpaRepository<ItemAcervo, Long> {

    List<ItemAcervo> findByUsuarioId(Long usuarioId);

    List<ItemAcervo> findByUsuarioIdAndStatus(Long usuarioId, StatusLeitura status);

    // Acrescentado para o filtro por status sem informar o usuário (GET /acervo?status=LENDO).
    List<ItemAcervo> findByStatus(StatusLeitura status);

    boolean existsByUsuarioIdAndLivroId(Long usuarioId, Long livroId);

    boolean existsByUsuarioId(Long usuarioId);

    boolean existsByLivroId(Long livroId);

}
