package denvot.homework.bookService.controllers;

import denvot.homework.bookService.controllers.responses.BookApiEntity;
import denvot.homework.bookService.data.entities.Book;

import denvot.homework.bookService.services.BooksServiceBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BooksControllerGetBooksByTagsTest {
  /*@MockBean
  private BooksServiceBase booksService;

  @Autowired
  private TestRestTemplate http;

  @Test
  public void testGetByTags() {
    var books = new ArrayList<Book>();
    books.add(new Book(1, "Джеффри Рихтер", "CLR via C#", Set.of("SomeTag")));
    books.add(new Book(2, "Кент Бек", "TDD", Set.of("SomeTag")));

    when(booksService.getBooksByTags(any())).thenReturn(books);

    ResponseEntity<BookApiEntity[]> booksResult = http.getForEntity("/api/books/tags/{tag}", BookApiEntity[].class, Map.of("tag", "SomeTag"));

    assertTrue(booksResult.getStatusCode().is2xxSuccessful());
    assertTrue(booksResult.hasBody());

    var body = booksResult.getBody();

    assertNotNull(body);
    assertEquals(2, body.length);
  }*/
}
