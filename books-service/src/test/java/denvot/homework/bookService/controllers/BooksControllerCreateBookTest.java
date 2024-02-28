package denvot.homework.bookService.controllers;

import denvot.homework.bookService.controllers.requests.BookCreationRequest;
import denvot.homework.bookService.controllers.responses.BookApiEntity;
import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.exceptions.InvalidBookDataException;
import denvot.homework.bookService.services.BooksServiceBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BooksControllerCreateBookTest {
  @MockBean
  private BooksServiceBase booksService;

  @Autowired
  private TestRestTemplate http;

  /*@Test
  public void testSimpleCreation() throws InvalidBookDataException {
    when(booksService.createNew(any()))
            .thenReturn(
                    new Book(0, "Джеффри Рихтер", "CLR via C#", Set.of("Записки сумасшедшего")));

    var bookCreationRequest = new BookCreationRequest("Джеффри Рихтер",
            "CLR via C#",
            new String[] {"Записки сумасшедшего"});

    ResponseEntity<BookApiEntity> response = http.postForEntity("/api/books", bookCreationRequest, BookApiEntity.class);

    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertTrue(response.hasBody());

    BookApiEntity result = response.getBody();

    assertNotNull(result);
    assertEquals("Джеффри Рихтер", result.author());
    assertEquals("CLR via C#", result.title());
    assertArrayEquals(new String[] {"Записки сумасшедшего"}, result.tags());
    verify(booksService).createNew(any());
  }

  @Test
  public void testCreationWithNullAuthorField() {
    var bookCreationRequest = new BookCreationRequest(null,
            "CLR via C#",
            new String[] {"Записки сумасшедшего"});

    testMust4xxError(bookCreationRequest);
  }

  @Test
  public void testCreationWithNullTitleField() {
    var bookCreationRequest = new BookCreationRequest("Джеффри Рихтер",
            null,
            new String[] {"Записки сумасшедшего"});

    testMust4xxError(bookCreationRequest);
  }

  @Test
  public void testCreationWithNullTagsField() {
    var bookCreationRequest = new BookCreationRequest("Джеффри Рихтер",
            "CLR via C#",
            null);

    testMust4xxError(bookCreationRequest);
  }

  private void testMust4xxError(BookCreationRequest request) {
    ResponseEntity<BookApiEntity> response = http.postForEntity("/api/books", request, BookApiEntity.class);

    assertTrue(response.getStatusCode().is4xxClientError());
  }*/
}
