package fr.iut.library.service;

import fr.iut.library.model.Author;
import fr.iut.library.model.Book;
import fr.iut.library.repository.AuthorRepository;
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
class BookServiceIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private Book book;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();

        Author author = new Author();
        author.setFirstName("George");
        author.setLastName("Orwell");
        author = authorRepository.save(author);

        book = new Book();
        book.setTitle("1984");
        book.setPublishYear(1949);
        book.setIsbn("9780451524935");
        book.setAuthor(author);
    }

    @Test
    void save_shouldPersistBook() {
        Book saved = bookService.save(book);

        assertThat(saved.getId()).isEqualTo(7L);
        assertThat(saved.getTitle()).isEqualTo("1984");
        assertThat(saved.getAuthor().getLastName()).isEqualTo("Orwell");
    }

    @Test
    void findAll_shouldReturnAllBooks() {
        bookRepository.save(book);

        List<Book> books = bookService.findAll();

        assertThat(books).hasSize(1);
        assertThat(books.get(0).getTitle()).isEqualTo("1984");
    }

    @Test
    void findById_shouldReturnBook_whenExists() {
        bookRepository.save(book);

        Optional<Book> found = bookService.findById(book.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("1984");
        assertThat(found.get().getAuthor().getFirstName()).isEqualTo("George");
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        Optional<Book> result = bookService.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void deleteById_shouldRemoveBook() {
        bookRepository.save(book);

        bookService.deleteById(book.getId());

        assertThat(bookRepository.findById(book.getId())).isEmpty();
    }
}
