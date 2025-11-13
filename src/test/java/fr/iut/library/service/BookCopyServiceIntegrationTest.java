package fr.iut.library.service;

import fr.iut.library.model.Author;
import fr.iut.library.model.Book;
import fr.iut.library.model.BookCopy;
import fr.iut.library.repository.AuthorRepository;
import fr.iut.library.repository.BookCopyRepository;
import fr.iut.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BookCopyServiceIntegrationTest {

    @Autowired
    private BookCopyService bookCopyService;

    @Autowired
    private BookCopyRepository bookCopyRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private BookCopy bookCopy;

    @BeforeEach
    void setUp() {
        bookCopyRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();

        // Création d’un auteur
        Author author = new Author();
        author.setFirstName("J.K.");
        author.setLastName("Rowling");
        author = authorRepository.save(author);

        // Création d’un livre
        Book book = new Book();
        book.setTitle("Harry Potter and the Philosopher's Stone");
        book.setPublishYear(1997);
        book.setIsbn("9780747532699");
        book.setAuthor(author);
        book = bookRepository.save(book);

        // Création d’un exemplaire du livre
        bookCopy = new BookCopy();
        bookCopy.setBook(book);
        bookCopy.setAvailable(true);
        bookCopy.setState("Good");
    }

    @Test
    void save_shouldPersistBookCopy() {
        BookCopy saved = bookCopyService.save(bookCopy);

        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved.getBook().getTitle()).isEqualTo("Harry Potter and the Philosopher's Stone");
        assertThat(saved.getAvailable()).isTrue();
        assertThat(saved.getState()).isEqualTo("Good");
    }

    @Test
    void findAll_shouldReturnAllBookCopies() {
        bookCopyRepository.save(bookCopy);

        List<BookCopy> copies = bookCopyService.findAll();

        assertThat(copies).hasSize(1);
        assertThat(copies.get(0).getState()).isEqualTo("Good");
    }

    @Test
    void findById_shouldReturnBookCopy_whenExists() {
        bookCopyRepository.save(bookCopy);

        Optional<BookCopy> found = bookCopyService.findById(bookCopy.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getBook().getAuthor().getLastName()).isEqualTo("Rowling");
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        Optional<BookCopy> result = bookCopyService.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void deleteById_shouldRemoveBookCopy() {
        bookCopyRepository.save(bookCopy);

        bookCopyService.deleteById(bookCopy.getId());

        assertThat(bookCopyRepository.findById(bookCopy.getId())).isEmpty();
    }
}
