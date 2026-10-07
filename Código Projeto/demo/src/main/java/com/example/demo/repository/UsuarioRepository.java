package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Usuario;

// JpaRepository já traz pronto: findAll, findById, save, delete, count...
// Aqui só acrescentamos o que é específico do Usuario.
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // O Spring lê o nome do método e monta a consulta sozinho:
    // "existe algum usuário com este email?"
    boolean existsByEmail(String email);
}
