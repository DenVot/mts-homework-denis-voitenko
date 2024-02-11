package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.entities.BookId;
import denvot.homework.bookService.data.repositories.BooksRepository;
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
  private BooksRepository repository;
  private BooksService booksService;

  @BeforeEach
  public void setUp() {
    repository = mock(BooksRepository.class);
    booksService = new BooksService(repository);
  }

  @Test
  public void testSimpleUpdate() throws BookNotFoundException {
    var bookToEdit = new Book(null, null, null, null);

    when(repository.findBook(any())).thenReturn(bookToEdit);

    var bookDeltaAuthor = new BookUpdateBuilder()
            .withAuthor("Кент Бек")
            .build();
    var bookDeltaTitle = new BookUpdateBuilder()
            .withTitle("TDD")
            .build();

    var tags = new HashSet<String>();

    var bookDeltaTags = new BookUpdateBuilder()
            .withNewTags(tags)
            .build();

    Optional<Book> newBook = booksService.updateBook(new BookId(1), bookDeltaAuthor);

    Assertions.assertTrue(newBook.isPresent());
    Assertions.assertEquals(bookToEdit, newBook.get());
    Assertions.assertEquals("Кент Бек", newBook.get().getAuthor());
    bookToEdit = new Book(null, null, null, null);
    when(repository.findBook(any())).thenReturn(bookToEdit);

    newBook = booksService.updateBook(new BookId(1), bookDeltaTitle);

    Assertions.assertTrue(newBook.isPresent());
    Assertions.assertEquals(bookToEdit, newBook.get());
    Assertions.assertEquals("TDD", newBook.get().getTitle());
    bookToEdit = new Book(null, null, null, null);
    when(repository.findBook(any())).thenReturn(bookToEdit);

    newBook = booksService.updateBook(new BookId(1), bookDeltaTags);

    Assertions.assertTrue(newBook.isPresent());
    Assertions.assertEquals(bookToEdit, newBook.get());
    Assertions.assertEquals(tags, newBook.get().getTags());
  }

  @Test
  public void testNotFoundUpdate() throws BookNotFoundException {
    when(repository.findBook(any())).thenThrow(new BookNotFoundException());

    var update = new BookUpdateBuilder().build();

    var newBook = booksService.updateBook(new BookId(1), update);
    Assertions.assertTrue(newBook.isEmpty());
  }
}
