package denvot.homework.bookService.controllers;

import denvot.homework.bookService.controllers.responses.BookApiEntity;
import denvot.homework.bookService.data.entities.Author;
import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.repositories.jpa.JpaAuthorsRepository;
import denvot.homework.bookService.data.repositories.jpa.JpaBooksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BooksControllerGetBookTest {
  @Autowired
  private JpaBooksRepository booksRepository;

  @Autowired
  private JpaAuthorsRepository authorsRepository;

  @Autowired
  private TestRestTemplate http;

  private Book testBook;

  @BeforeEach
  public void setUp() {
    booksRepository.deleteAll();
    authorsRepository.deleteAll();

    var testAuthor = new Author("Test", "Author");
    authorsRepository.save(testAuthor);

    testBook = new Book("Test Book", testAuthor);
    booksRepository.save(testBook);
  }

  @Test
  public void testSimpleGetBook() {
    ResponseEntity<BookApiEntity> response = http.getForEntity("/api/books/{id}", BookApiEntity.class, Map.of("id", testBook.getId()));

    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertTrue(response.hasBody());

    var body = response.getBody();

    assertNotNull(body);
    assertEquals(testBook.getTitle(), body.title());
  }

  @Test
  public void testBookNotFound() {
    ResponseEntity<BookApiEntity> response = http.getForEntity("/api/books/{id}", BookApiEntity.class, Map.of("id", testBook.getId() + 1));

    assertTrue(response.getStatusCode().is4xxClientError());
  }
}
