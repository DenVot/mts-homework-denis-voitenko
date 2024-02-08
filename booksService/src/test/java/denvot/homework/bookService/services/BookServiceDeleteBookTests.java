package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.BookId;
import denvot.homework.bookService.data.repositories.BooksRepository;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceDeleteBookTests {
  private BooksRepository repository;
  private BooksService booksService;

  @BeforeEach
  public void setUp() {
    repository = mock(BooksRepository.class);
    booksService = new BooksService(repository);
  }

  @Test
  public void testSimpleDelete() throws BookNotFoundException {
    boolean isDeleted = booksService.deleteBook(new BookId(1));

    verify(repository).deleteBook(any());
    Assertions.assertTrue(isDeleted);
  }

  @Test
  public void testDeleteBookNotExists() throws BookNotFoundException {
    doThrow(new BookNotFoundException()).when(repository).deleteBook(any());

    boolean isDeleted = booksService.deleteBook(new BookId(1));

    verify(repository).deleteBook(any());
    Assertions.assertFalse(isDeleted);
  }
}
