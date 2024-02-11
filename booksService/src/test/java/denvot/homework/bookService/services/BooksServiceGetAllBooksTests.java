package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.entities.BookId;
import denvot.homework.bookService.data.repositories.BooksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BooksServiceGetAllBooksTests {
  private BooksRepository repository;
  private BooksService booksService;

  @BeforeEach
  public void setUp() {
    repository = mock(BooksRepository.class);
    booksService = new BooksService(repository);
  }

  @Test
  public void testGetAllBooks() {
    var testBooks = List.of(
            new Book(new BookId(1), "Джеффри Рихтер", "CLR via C#", Set.of("Записки сумасшедшего")),
            new Book(new BookId(2), "Кент Бек", "TDD", Set.of("Записки гения")),
            new Book(new BookId(3), "Джоан Роулинг", "Гарри Поттер", Set.of("Чьи-то записки")));

    when(repository.getAllBooks()).thenReturn(testBooks);

    List<Book> books = booksService.getAllBooks();

    assertEquals(testBooks, books);
  }
}
