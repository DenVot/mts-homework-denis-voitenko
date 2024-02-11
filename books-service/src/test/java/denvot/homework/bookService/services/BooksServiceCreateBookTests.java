package denvot.homework.bookService.services;

import denvot.homework.bookService.data.repositories.BooksRepositoryBase;
import denvot.homework.bookService.exceptions.InvalidBookDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class BooksServiceCreateBookTests {
  private BooksRepositoryBase repository;
  private BooksService booksService;

  @BeforeEach
  public void setUp() {
    repository = mock(BooksRepositoryBase.class);
    booksService = new BooksService(repository);
  }

  @Test
  public void testSimpleAddition() throws InvalidBookDataException {
    var tags = new HashSet<String>();
    tags.add("Романы");

    BookCreationInfo info = new BookCreationInfo(
            "Кент Бек",
            "Экстремальное программирование. Разработка через тестирование",
            tags);

    booksService.createNew(info);

    verify(repository).createBook(any(), any(), any());
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
            () -> booksService.createNew(infoWithNullAuthor));

    Assertions.assertThrows(InvalidBookDataException.class,
            () -> booksService.createNew(infoWithNullTitle));

    Assertions.assertThrows(InvalidBookDataException.class,
            () -> booksService.createNew(infoWithNullTags));
  }
}
