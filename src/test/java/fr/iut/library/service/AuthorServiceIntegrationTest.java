package fr.iut.library.service;

import fr.iut.library.model.Author;
import fr.iut.library.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AuthorServiceIntegrationTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;

    private Author author;

    @BeforeEach
    void setUp() {
        authorRepository.deleteAll(); // Nettoyage avant chaque test

        author = new Author();
        author.setFirstName("Victor");
        author.setLastName("Hugo");
    }

    @Test
    void save_shouldPersistAuthor() {
        Author saved = authorService.save(author);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getFirstName()).isEqualTo("Victor");
        assertThat(saved.getLastName()).isEqualTo("Hugo");

        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(1);
        assertThat(authors.get(0).getLastName()).isEqualTo("Hugo");
    }

    @Test
    void findAll_shouldReturnAllAuthors() {
        authorRepository.save(author);

        List<Author> authors = authorService.findAll();

        assertThat(authors).hasSize(1);
        assertThat(authors.get(0).getFirstName()).isEqualTo("Victor");
    }

    @Test
    void findById_shouldReturnAuthor_whenExists() {
        Author saved = authorRepository.save(author);

        Optional<Author> found = authorService.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getLastName()).isEqualTo("Hugo");
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        Optional<Author> result = authorService.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void deleteById_shouldRemoveAuthor() {
        Author saved = authorRepository.save(author);
        Long id = saved.getId();

        authorService.deleteById(id);

        assertThat(authorRepository.findById(id)).isEmpty();
    }
}
