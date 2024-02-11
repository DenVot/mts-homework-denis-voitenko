package denvot.homework.bookService.data.repositories;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.entities.BookId;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

public class HashMapBooksRepositoryDeleteBookTests {
  private HashMap<BookId, Book> books;
  private HashMapBooksRepository repository;

  @BeforeEach
  public void setUp() {
    books = new HashMap<>();
    repository = new HashMapBooksRepository(books);
  }

  @Test
  public void testSimpleDeletion() throws BookNotFoundException {
    var id = new BookId(1);
    var book = new Book(id, "Кент Бек", "TDD", new HashSet<>());

    books.put(id, book);
    repository.deleteBook(id);

    Assertions.assertTrue(books.isEmpty());
  }

  @Test
  public void testDeletionOfBookNotExists() {
    Assertions.assertThrows(BookNotFoundException.class,
            () -> repository.deleteBook(new BookId(1)));
  }
}
