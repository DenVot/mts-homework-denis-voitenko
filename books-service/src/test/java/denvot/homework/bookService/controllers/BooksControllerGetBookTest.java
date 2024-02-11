package denvot.homework.bookService.controllers;

import denvot.homework.bookService.controllers.responses.BookApiEntity;
import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.entities.BookId;
import denvot.homework.bookService.services.BooksServiceBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BooksControllerGetBookTest {
  @MockBean
  private BooksServiceBase booksService;

  @Autowired
  private TestRestTemplate http;

  @Test
  public void testSimpleGetBook() {
    when(booksService.findBook(any()))
            .thenReturn(Optional.of(
                    new Book(new BookId(0), "Кент Бек", "TDD", Set.of("Записки гения"))));

    ResponseEntity<BookApiEntity> response = http.getForEntity("/api/books/{id}", BookApiEntity.class, Map.of("id", 0));

    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertTrue(response.hasBody());

    var body = response.getBody();

    assertNotNull(body);
    assertEquals("Кент Бек", body.author());
    assertEquals("TDD", body.title());
    assertArrayEquals(new String[] {"Записки гения"}, body.tags());
    verify(booksService).findBook(any());
  }

  @Test
  public void testBookNotFound() {
    when(booksService.findBook(any())).thenReturn(Optional.empty());

    ResponseEntity<BookApiEntity> response = http.getForEntity("/api/books/{id}", BookApiEntity.class, Map.of("id", 0));

    assertTrue(response.getStatusCode().is4xxClientError());
  }
}
