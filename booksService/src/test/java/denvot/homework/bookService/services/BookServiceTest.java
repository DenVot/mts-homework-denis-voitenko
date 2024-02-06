package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.entities.BookId;
import denvot.homework.bookService.data.repositories.HashMapBooksRepository;
import denvot.homework.bookService.exceptions.InvalidBookDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

class BookServiceTest {
  private BookService bookService;
  private HashMap<BookId, Book> hashTable;


  @BeforeEach
  public void setUp() {
    hashTable = new HashMap<>();
    HashMapBooksRepository bookRepository = new HashMapBooksRepository(hashTable);
    bookService = new BookService(bookRepository);
  }

  @Test
  public void testSimpleAddition() throws InvalidBookDataException {
    var tags = new HashSet<String>();
    tags.add("Романы");

    var createdBook = bookService.createNew("Кент Бек", "Экстремальное программирование. Разработка через тестирование", tags);

    Assertions.assertNotNull(createdBook);
    Assertions.assertTrue(hashTable.containsKey(createdBook.getId()));
    Assertions.assertEquals(createdBook, hashTable.get(createdBook.getId()));
  }

  @Test
  public void testAdditionWhenSomethingNull() {
    var tags = new HashSet<String>();
    tags.add("Романы");

    Assertions.assertThrows(InvalidBookDataException.class, () -> {
      bookService.createNew(null, "Экстремальное программирование. Разработка через тестирование", tags);
    });

    Assertions.assertThrows(InvalidBookDataException.class, () -> {
      bookService.createNew("Кент Бек", null, tags);
    });

    Assertions.assertThrows(InvalidBookDataException.class, () -> {
      bookService.createNew("Кент Бек", "Экстремальное программирование. Разработка через тестирование", null);
    });
  }
}
