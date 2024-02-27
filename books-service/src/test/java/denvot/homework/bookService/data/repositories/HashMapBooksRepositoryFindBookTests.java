package denvot.homework.bookService.data.repositories;

import denvot.homework.bookService.data.entities.Book;

import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

public class HashMapBooksRepositoryFindBookTests {
  private HashMap<Long, Book> hashMap;
  private HashMapBooksRepository repository;

  @BeforeEach
  public void setUp() {
    hashMap = new HashMap<>();
    repository = new HashMapBooksRepository(hashMap);
  }

  @Test
  public void testSimpleFind() throws BookNotFoundException {
    var id = 1L;
    var book = new Book(id, "Кент Бек", "TDD", new HashSet<>());

    hashMap.put(id, book);

    Book result = repository.findBook(1L);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(result, book);
  }

  @Test
  public void testItemNotFound() {
    Assertions.assertThrows(BookNotFoundException.class,
            () -> repository.findBook(1L));
  }
}
