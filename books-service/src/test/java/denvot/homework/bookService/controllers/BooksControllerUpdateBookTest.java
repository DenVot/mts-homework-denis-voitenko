package denvot.homework.bookService.controllers;

import denvot.homework.bookService.controllers.requests.BookUpdateRequest;
import denvot.homework.bookService.controllers.responses.BookApiEntity;
import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.services.BooksServiceBase;
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

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BooksControllerUpdateBookTest {
  @MockBean
  private BooksServiceBase booksService;

  @Autowired
  private TestRestTemplate http;

  @BeforeEach
  public void setUp() {
    http.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
  }

  @Test
  public void testUpdateWithChanges() {
    var testBook = Optional.of(new Book(1, "Кент Бек", "TDD", Set.of("Записки гения")));

    when(booksService.updateBookAuthor(anyLong(), any())).thenReturn(testBook);
    when(booksService.updateBookTags(anyLong(), any())).thenReturn(testBook);
    when(booksService.updateBookTitle(anyLong(), any())).thenReturn(testBook);

    BookUpdateRequest updateRequest = new BookUpdateRequest("Джеффри Рихтер", "CLR via C#", new String[] {"Записки сумасшедшего"});
    HttpEntity<BookUpdateRequest> updateRequestEntity = new HttpEntity<>(updateRequest);
    ResponseEntity<BookApiEntity> response = http.exchange("/api/books/{id}", HttpMethod.PATCH, updateRequestEntity, BookApiEntity.class, Map.of("id", 1));

    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertTrue(response.hasBody());

    BookApiEntity body = response.getBody();

    assertNotNull(body);
    assertEquals("Кент Бек", body.author());
    assertEquals("TDD", body.title());
    assertArrayEquals(new String[] {"Записки гения"}, body.tags());
    verify(booksService).updateBookAuthor(anyLong(), any());
    verify(booksService).updateBookTitle(anyLong(), any());
    verify(booksService).updateBookTags(anyLong(), any());
  }

  @Test
  public void testUpdateNotFound() {
    when(booksService.updateBookAuthor(anyLong(), any())).thenReturn(Optional.empty());
    when(booksService.updateBookTags(anyLong(), any())).thenReturn(Optional.empty());
    when(booksService.updateBookTitle(anyLong(), any())).thenReturn(Optional.empty());

    BookUpdateRequest updateRequest = new BookUpdateRequest("Джеффри Рихтер", "CLR via C#", new String[] {"Записки сумасшедшего"});
    HttpEntity<BookUpdateRequest> updateRequestEntity = new HttpEntity<>(updateRequest);
    ResponseEntity<BookApiEntity> response = http.exchange("/api/books/{id}", HttpMethod.PATCH, updateRequestEntity, BookApiEntity.class, Map.of("id", 1));

    assertTrue(response.getStatusCode().is4xxClientError());
  }

  @Test
  public void testNoUpdate() {
    var testBook = Optional.of(new Book(1, "Кент Бек", "TDD", Set.of("Записки гения")));

    when(booksService.updateBookAuthor(anyLong(), any())).thenReturn(testBook);
    when(booksService.updateBookTags(anyLong(), any())).thenReturn(testBook);
    when(booksService.updateBookTitle(anyLong(), any())).thenReturn(testBook);
    BookUpdateRequest updateRequest = new BookUpdateRequest(null, null, null);

    HttpEntity<BookUpdateRequest> updateRequestEntity = new HttpEntity<>(updateRequest);
    ResponseEntity<BookApiEntity> response = http.exchange("/api/books/{id}", HttpMethod.PATCH, updateRequestEntity, BookApiEntity.class, Map.of("id", 1));

    assertTrue(response.getStatusCode().is4xxClientError());
  }
}
