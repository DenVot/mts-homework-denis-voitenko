package denvot.homework.bookService.services;


import denvot.homework.bookService.data.repositories.BooksRepositoryBase;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class BooksServiceDeleteBookTests {
  private BooksRepositoryBase repository;
  private BooksService booksService;

  @BeforeEach
  public void setUp() {
    repository = mock(BooksRepositoryBase.class);
    booksService = new BooksService(repository);
  }

  @Test
  public void testSimpleDelete() throws BookNotFoundException {
    boolean isDeleted = booksService.deleteBook(1L);

    verify(repository).deleteBook(1L);
    Assertions.assertTrue(isDeleted);
  }

  @Test
  public void testDeleteBookNotExists() throws BookNotFoundException {
    doThrow(new BookNotFoundException()).when(repository).deleteBook(1L);

    boolean isDeleted = booksService.deleteBook(1L);

    verify(repository).deleteBook(1L);
    Assertions.assertFalse(isDeleted);
  }
}
