package com.example.demo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.Usuario;
import com.example.demo.repository.ItemAcervoRepository;
import com.example.demo.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ItemAcervoRepository itemAcervoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, ItemAcervoRepository itemAcervoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.itemAcervoRepository = itemAcervoRepository;
    }

    // GET /usuarios
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    // GET /usuarios/{id}
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuário não encontrado: " + id));
    }

    // POST /usuarios
    public Usuario criar(Usuario usuario) {
        validar(usuario);
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com este email");
        }
        usuario.setId(null);
        return usuarioRepository.save(usuario);
    }

    // PUT /usuarios/{id}
    public Usuario atualizar(Long id, Usuario dados) {
        Usuario usuario = buscarPorId(id);
        validar(dados);
        boolean emailMudou = !dados.getEmail().equalsIgnoreCase(usuario.getEmail());
        if (emailMudou && usuarioRepository.existsByEmail(dados.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com este email");
        }
        usuario.setNome(dados.getNome());
        usuario.setEmail(dados.getEmail());
        return usuarioRepository.save(usuario);
    }

    // DELETE /usuarios/{id}
    public void excluir(Long id) {
        Usuario usuario = buscarPorId(id);
        if (itemAcervoRepository.existsByUsuarioId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível excluir: o usuário ainda tem livros no acervo");
        }
        usuarioRepository.delete(usuario);
    }

    private void validar(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome é obrigatório");
        }
        if (usuario.getEmail() == null || !usuario.getEmail().contains("@")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um email válido");
        }
    }
}
