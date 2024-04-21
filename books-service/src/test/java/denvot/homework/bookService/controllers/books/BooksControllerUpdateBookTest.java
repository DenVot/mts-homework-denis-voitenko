package denvot.homework.bookService.controllers.books;

import denvot.homework.bookService.DatabaseSuite;
import denvot.homework.bookService.TestHelper;
import denvot.homework.bookService.controllers.requests.BookUpdateRequest;
import denvot.homework.bookService.controllers.responses.BookApiEntity;
import denvot.homework.bookService.data.entities.*;
import denvot.homework.bookService.data.repositories.jpa.JpaAuthorsRepository;
import denvot.homework.bookService.data.repositories.jpa.JpaBooksRepository;
import denvot.homework.bookService.data.repositories.jpa.JpaTagsRepository;
import denvot.homework.bookService.data.repositories.jpa.JpaUserRepository;
import denvot.homework.bookService.services.BooksPurchasingManagerBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BooksControllerUpdateBookTest extends DatabaseSuite {
  @Autowired
  private JpaBooksRepository booksRepository;

  @Autowired
  private JpaAuthorsRepository authorsRepository;

  @Autowired
  private TestRestTemplate http;

  @Autowired
  private JpaTagsRepository tagsRepository;

  @Autowired
  private JpaUserRepository userRepository;

  @Autowired
  private PasswordEncoder encoder;

  @MockBean
  private BooksPurchasingManagerBase booksPurchasingManager;

  private Book testBook;
  private Author testAuthor;

  @BeforeEach
  public void authSetup() {
    userRepository.deleteAll();
    userRepository.save(new User("Test", encoder.encode("User"), Set.of(new Role("ADMIN"))));
    http = http.withBasicAuth("Test", "User");
  }

  @BeforeEach
  public void setUp() {
    booksRepository.deleteAll();
    authorsRepository.deleteAll();
    tagsRepository.deleteAll();

    testAuthor = new Author("Test", "Author");
    authorsRepository.save(testAuthor);

    testBook = new Book("Test Book", testAuthor);
    booksRepository.save(testBook);
    http.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
  }

  @Test
  public void testUpdateWithChanges() {
    BookUpdateRequest updateRequest = new BookUpdateRequest(testAuthor.getId(), "CLR via C#");
    HttpEntity<BookUpdateRequest> updateRequestEntity = new HttpEntity<>(updateRequest);
    ResponseEntity<BookApiEntity> response = http.exchange("/api/books/{id}", HttpMethod.PATCH, updateRequestEntity, BookApiEntity.class, Map.of("id", testBook.getId()));

    BookApiEntity body = TestHelper.assert2xxAndGetBody(response);

    assertNotNull(body);
    assertEquals(testAuthor.getId(), body.author().id());
    assertEquals("CLR via C#", body.title());
  }

  @Test
  public void testUpdateNotFound() {
    BookUpdateRequest updateRequest = new BookUpdateRequest(1L, "CLR via C#");
    HttpEntity<BookUpdateRequest> updateRequestEntity = new HttpEntity<>(updateRequest);
    ResponseEntity<BookApiEntity> response = http.exchange("/api/books/{id}", HttpMethod.PATCH, updateRequestEntity, BookApiEntity.class, Map.of("id", testBook.getId() + 1));

    assertTrue(response.getStatusCode().is4xxClientError());
  }

  @Test
  public void testNoUpdate() {
    BookUpdateRequest updateRequest = new BookUpdateRequest(null, null);

    HttpEntity<BookUpdateRequest> updateRequestEntity = new HttpEntity<>(updateRequest);
    ResponseEntity<BookApiEntity> response = http.exchange("/api/books/{id}", HttpMethod.PATCH, updateRequestEntity, BookApiEntity.class, Map.of("id", testBook.getId()));

    assertTrue(response.getStatusCode().is4xxClientError());
  }

  @Test
  public void testUpdateNewTag() {
    Tag testTag = new Tag("Test Tag");
    tagsRepository.save(testTag);

    ResponseEntity<BookApiEntity> response = http.exchange("/api/books/{book_id}/tags/{tag_id}", HttpMethod.PATCH,
            null, BookApiEntity.class, Map.of("book_id", testBook.getId(), "tag_id", testTag.getId()));

    BookApiEntity body = TestHelper.assert2xxAndGetBody(response);

    assertNotNull(body);
    assertEquals(testAuthor.getId(), body.author().id());
    assertEquals(1, body.tags().size());
    assertEquals(testTag.getId(), body.tags().get(0).id());
  }

  @Test
  public void testUpdateNewTagTargetEntitiesNotFound() {
    Tag testTag = new Tag("Test Tag");
    tagsRepository.save(testTag);

    ResponseEntity<BookApiEntity> response = http.exchange("/api/books/{book_id}/tags/{tag_id}", HttpMethod.PATCH,
            null, BookApiEntity.class, Map.of("book_id", testBook.getId(), "tag_id", testTag.getId() + 1));

    assertTrue(response.getStatusCode().is4xxClientError());

    response = http.exchange("/api/books/{book_id}/tags/{tag_id}", HttpMethod.PATCH,
            null, BookApiEntity.class, Map.of("book_id", testBook.getId() + 1, "tag_id", testTag.getId()));

    assertTrue(response.getStatusCode().is4xxClientError());

    response = http.exchange("/api/books/{book_id}/tags/{tag_id}", HttpMethod.PATCH,
            null, BookApiEntity.class, Map.of("book_id", testBook.getId() + 1, "tag_id", testTag.getId() + 1));

    assertTrue(response.getStatusCode().is4xxClientError());
  }

  @Test
  public void testUpdateRemoveTag() {
    Tag testTag = new Tag("Test Tag");
    tagsRepository.save(testTag);

    testBook.assignTag(testTag);
    booksRepository.save(testBook);

    ResponseEntity<BookApiEntity> response = http.exchange("/api/books/{book_id}/tags/{tag_id}", HttpMethod.DELETE,
            null, BookApiEntity.class, Map.of("book_id", testBook.getId(), "tag_id", testTag.getId()));

    BookApiEntity body = TestHelper.assert2xxAndGetBody(response);

    assertNotNull(body);
    assertEquals(testAuthor.getId(), body.author().id());
    assertEquals(0, body.tags().size());
  }

  @Test
  public void testUpdateRemoveTagTargetEntitiesNotFound() {
    Tag testTag = new Tag("Test Tag");
    tagsRepository.save(testTag);

    ResponseEntity<BookApiEntity> response = http.exchange("/api/books/{book_id}/tags/{tag_id}", HttpMethod.DELETE,
            null, BookApiEntity.class, Map.of("book_id", testBook.getId(), "tag_id", testTag.getId() + 1));

    assertTrue(response.getStatusCode().is4xxClientError());

    response = http.exchange("/api/books/{book_id}/tags/{tag_id}", HttpMethod.DELETE,
            null, BookApiEntity.class, Map.of("book_id", testBook.getId() + 1, "tag_id", testTag.getId()));

    assertTrue(response.getStatusCode().is4xxClientError());

    response = http.exchange("/api/books/{book_id}/tags/{tag_id}", HttpMethod.DELETE,
            null, BookApiEntity.class, Map.of("book_id", testBook.getId() + 1, "tag_id", testTag.getId() + 1));

    assertTrue(response.getStatusCode().is4xxClientError());
  }
}
