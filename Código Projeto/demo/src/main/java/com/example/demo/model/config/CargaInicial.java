package com.example.demo.model.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.model.ItemAcervo;
import com.example.demo.model.Livro;
import com.example.demo.model.StatusLeitura;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ItemAcervoRepository;
import com.example.demo.repository.LivroRepository;
import com.example.demo.repository.UsuarioRepository;

// Como o banco H2 fica na memória, ele nasce vazio toda vez que a aplicação sobe.
// Esta classe roda uma vez na inicialização e cadastra alguns dados de exemplo,
// para o Swagger UI já mostrar algo e a demonstração do pitch não começar do zero.
@Component
public class CargaInicial implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;
    private final ItemAcervoRepository itemAcervoRepository;

    public CargaInicial(UsuarioRepository usuarioRepository,
                        LivroRepository livroRepository,
                        ItemAcervoRepository itemAcervoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
        this.itemAcervoRepository = itemAcervoRepository;
    }

    @Override
    public void run(String... args) {
        Usuario leitor = usuarioRepository.save(new Usuario("Leitor de Exemplo", "leitor@bookbyte.com"));

        Livro livro1984 = livroRepository.save(new Livro(
                "978-85-828-5119-7", "1984", "George Orwell", "Penguin-Companhia das Letras",
                "Português", 392, LocalDate.of(2020, 11, 21),
                "https://covers.openlibrary.org/b/isbn/9788582851197-L.jpg"));

        Livro pequenoPrincipe = livroRepository.save(new Livro(
                "978-85-220-3145-0", "O Pequeno Príncipe", "Antoine de Saint-Exupéry", "Agir",
                "Português", 96, LocalDate.of(2015, 5, 1),
                "https://covers.openlibrary.org/b/isbn/9788522031450-L.jpg"));

        Livro senhorDosAneis = livroRepository.save(new Livro(
                "978-85-336-1516-8", "O Senhor dos Anéis", "J. R. R. Tolkien", "Martins Fontes",
                "Português", 1211, LocalDate.of(2001, 1, 1),
                "https://covers.openlibrary.org/b/isbn/9788533613409-L.jpg"));

        itemAcervoRepository.save(new ItemAcervo(leitor, livro1984, StatusLeitura.LENDO, 200));
        itemAcervoRepository.save(new ItemAcervo(leitor, pequenoPrincipe, StatusLeitura.CONCLUIDO, 96));
        itemAcervoRepository.save(new ItemAcervo(leitor, senhorDosAneis, StatusLeitura.QUERO_LER, 0));
    }
}
