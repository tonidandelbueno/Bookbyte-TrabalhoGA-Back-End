package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    // "buscar livros cujo título CONTÉM este texto, ignorando maiúsculas/minúsculas"
    List<Livro> findByTituloContainingIgnoreCase(String titulo);
}
