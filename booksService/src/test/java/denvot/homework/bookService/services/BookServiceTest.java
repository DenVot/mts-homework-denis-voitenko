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

    BookCreationInfo info = new BookCreationInfo(
            "Кент Бек",
            "Экстремальное программирование. Разработка через тестирование",
            tags);

    var createdBook = bookService.createNew(info);

    Assertions.assertNotNull(createdBook);
    Assertions.assertTrue(hashTable.containsKey(createdBook.getId()));
    Assertions.assertEquals(createdBook, hashTable.get(createdBook.getId()));
  }

  @Test
  public void testAdditionWhenSomethingNull() {
    var tags = new HashSet<String>();
    tags.add("Романы");

    BookCreationInfo infoWithNullAuthor = new BookCreationInfo(
            null,
            "Экстремальное программирование. Разработка через тестирование",
            tags);
    BookCreationInfo infoWithNullTitle = new BookCreationInfo(
            "Кент Бек",
            null,
            tags);
    BookCreationInfo infoWithNullTags = new BookCreationInfo(
            "Кент Бек",
            "Экстремальное программирование. Разработка через тестирование",
            null);

    Assertions.assertThrows(InvalidBookDataException.class,
            () -> bookService.createNew(infoWithNullAuthor));

    Assertions.assertThrows(InvalidBookDataException.class,
            () -> bookService.createNew(infoWithNullTitle));

    Assertions.assertThrows(InvalidBookDataException.class,
            () -> bookService.createNew(infoWithNullTags));
  }
}
