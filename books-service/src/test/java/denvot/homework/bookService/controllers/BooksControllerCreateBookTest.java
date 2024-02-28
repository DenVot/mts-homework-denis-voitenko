package denvot.homework.bookService.controllers;

import denvot.homework.bookService.DatabaseSuite;
import denvot.homework.bookService.controllers.requests.BookCreationRequest;
import denvot.homework.bookService.controllers.responses.BookApiEntity;
import denvot.homework.bookService.data.entities.Author;
import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.repositories.jpa.JpaAuthorsRepository;
import denvot.homework.bookService.data.repositories.jpa.JpaBooksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class BooksControllerCreateBookTest extends DatabaseSuite {
  @Autowired
  private JpaBooksRepository booksRepository;

  @Autowired
  private JpaAuthorsRepository authorsRepository;

  @Autowired
  private TestRestTemplate http;

  private Book testBook;
  private Author testAuthor;

  @BeforeEach
  public void setUp() {
    booksRepository.deleteAll();
    authorsRepository.deleteAll();

    testAuthor = new Author("Test", "Author");
    authorsRepository.save(testAuthor);

    testBook = new Book("Test Book", testAuthor);
    booksRepository.save(testBook);
  }

  @Test
  public void testSimpleCreation() {
    var bookCreationRequest = new BookCreationRequest(testAuthor.getId(), testBook.getTitle());

    ResponseEntity<BookApiEntity> response = http.postForEntity("/api/books", bookCreationRequest, BookApiEntity.class);

    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertTrue(response.hasBody());

    BookApiEntity result = response.getBody();

    assertNotNull(result);
    assertEquals(testAuthor.getId(), result.author().id());
    assertEquals(testBook.getTitle(), result.title());
  }

  @Test
  public void testCreationWithNullAuthorField() {
    var bookCreationRequest = new BookCreationRequest(null,
            "CLR via C#");

    testMust4xxError(bookCreationRequest);
  }

  @Test
  public void testCreationWithNullTitleField() {
    var bookCreationRequest = new BookCreationRequest(0L,
            null);

    testMust4xxError(bookCreationRequest);
  }

  private void testMust4xxError(BookCreationRequest request) {
    ResponseEntity<BookApiEntity> response = http.postForEntity("/api/books", request, BookApiEntity.class);

    assertTrue(response.getStatusCode().is4xxClientError());
  }
}
