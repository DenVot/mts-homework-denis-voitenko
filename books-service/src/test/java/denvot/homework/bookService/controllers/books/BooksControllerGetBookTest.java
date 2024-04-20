package denvot.homework.bookService.controllers.books;

import denvot.homework.bookService.DatabaseSuite;
import denvot.homework.bookService.TestHelper;
import denvot.homework.bookService.controllers.responses.BookApiEntity;
import denvot.homework.bookService.data.entities.Author;
import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.entities.Role;
import denvot.homework.bookService.data.entities.User;
import denvot.homework.bookService.data.repositories.jpa.JpaAuthorsRepository;
import denvot.homework.bookService.data.repositories.jpa.JpaBooksRepository;
import denvot.homework.bookService.data.repositories.jpa.JpaUserRepository;
import denvot.homework.bookService.services.BooksPurchasingManagerBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BooksControllerGetBookTest extends DatabaseSuite {
  @Autowired
  private JpaBooksRepository booksRepository;

  @Autowired
  private JpaAuthorsRepository authorsRepository;

  @Autowired
  private JpaUserRepository userRepository;

  @Autowired
  private TestRestTemplate http;

  @Autowired
  private PasswordEncoder encoder;

  @MockBean
  private BooksPurchasingManagerBase booksPurchasingManager;

  private Book testBook;

  @BeforeEach
  public void setUp() {
    booksRepository.deleteAll();
    authorsRepository.deleteAll();
    userRepository.deleteAll();

    var testAuthor = new Author("Test", "Author");
    authorsRepository.save(testAuthor);

    testBook = new Book("Test Book", testAuthor);
    booksRepository.save(testBook);

    userRepository.save(new User("Test", encoder.encode("User"), Set.of(new Role("ADMIN"))));
    http = http.withBasicAuth("Test", "User");
  }

  @Test
  public void testSimpleGetBook() {
    ResponseEntity<BookApiEntity> response = http.getForEntity("/api/books/{id}", BookApiEntity.class, Map.of("id", testBook.getId()));

    BookApiEntity body = TestHelper.assert2xxAndGetBody(response);

    assertNotNull(body);
    assertEquals(testBook.getTitle(), body.title());
  }

  @Test
  public void testBookNotFound() {
    ResponseEntity<BookApiEntity> response = http.getForEntity("/api/books/{id}", BookApiEntity.class, Map.of("id", testBook.getId() + 1));

    assertTrue(response.getStatusCode().is4xxClientError());
  }
}
