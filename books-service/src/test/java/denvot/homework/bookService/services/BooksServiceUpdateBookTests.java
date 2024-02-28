package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.entities.BookId;
import denvot.homework.bookService.data.repositories.BooksRepositoryBase;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BooksServiceUpdateBookTests {
  private BooksRepositoryBase repository;
  private BooksService booksService;

  @BeforeEach
  public void setUp() {
    repository = mock(BooksRepositoryBase.class);
    booksService = new BooksService(repository);
  }

  @Test
  public void testSimpleUpdate() throws BookNotFoundException {
    var bookToEdit = new Book(null, null, null, null);

    when(repository.findBook(any())).thenReturn(bookToEdit);

    Optional<Book> newBook = booksService.updateBookAuthor(new BookId(1), "Кент Бек");
    Assertions.assertTrue(newBook.isPresent());
    Assertions.assertEquals(bookToEdit, newBook.get());
    Assertions.assertEquals("Кент Бек", newBook.get().getAuthor());

    bookToEdit = new Book(null, null, null, null);
    when(repository.findBook(any())).thenReturn(bookToEdit);

    newBook = booksService.updateBookTitle(new BookId(1), "TDD");

    Assertions.assertTrue(newBook.isPresent());
    Assertions.assertEquals(bookToEdit, newBook.get());
    Assertions.assertEquals("TDD", newBook.get().getTitle());

    bookToEdit = new Book(null, null, null, null);
    when(repository.findBook(any())).thenReturn(bookToEdit);

    var tags = new HashSet<String>();
    newBook = booksService.updateBookTags(new BookId(1), tags);

    Assertions.assertTrue(newBook.isPresent());
    Assertions.assertEquals(bookToEdit, newBook.get());
    Assertions.assertEquals(tags, newBook.get().getTags());
  }

  @Test
  public void testNotFoundUpdate() throws BookNotFoundException {
    when(repository.findBook(any())).thenThrow(new BookNotFoundException());

    var newBook = booksService.updateBookAuthor(new BookId(1), "Кент Бек");
    Assertions.assertTrue(newBook.isEmpty());
  }
}
