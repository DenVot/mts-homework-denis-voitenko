package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.entities.BookId;
import denvot.homework.bookService.data.repositories.BooksRepositoryBase;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BooksServiceFindBooksTests {
  private BooksRepositoryBase repository;
  private BooksService booksService;

  @BeforeEach
  public void setUp() {
    repository = mock(BooksRepositoryBase.class);
    booksService = new BooksService(repository);
  }

  @Test
  public void testSimpleFind() throws BookNotFoundException {
    var expectedBook = new Book(null, null, null, null);
    when(repository.findBook(any())).thenReturn(expectedBook);

    var result = booksService.findBook(new BookId(1));

    Assertions.assertTrue(result.isPresent());
    Assertions.assertEquals(expectedBook, result.get());
  }

  @Test
  public void testEmptyBook() throws BookNotFoundException {
    when(repository.findBook(any())).thenThrow(new BookNotFoundException());

    var result = booksService.findBook(new BookId(1));

    Assertions.assertFalse(result.isPresent());
  }
}
