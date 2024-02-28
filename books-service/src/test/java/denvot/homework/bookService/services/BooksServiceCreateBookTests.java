package denvot.homework.bookService.services;

import denvot.homework.bookService.DatabaseSuite;
import denvot.homework.bookService.data.entities.Author;
import denvot.homework.bookService.data.repositories.DbBooksRepository;
import denvot.homework.bookService.data.repositories.jpa.JpaAuthorsRepository;
import denvot.homework.bookService.data.repositories.jpa.JpaBooksRepository;
import denvot.homework.bookService.exceptions.InvalidBookDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import({BooksService.class, DbBooksRepository.class})
class BooksServiceCreateBookTests extends DatabaseSuite {
  @Autowired
  private JpaAuthorsRepository jpaAuthorsRepository;

  @Autowired
  private JpaBooksRepository jpaBooksRepository;

  @Autowired
  private BooksService booksService;

  private Author testAuthor;

  @BeforeEach
  public void setUp() {
    jpaBooksRepository.deleteAll();
    jpaAuthorsRepository.deleteAll();
    testAuthor = jpaAuthorsRepository.save(new Author("Test", "Author"));
  }

  @Test
  public void testSimpleAddition() {
    BookCreationInfo info = new BookCreationInfo(
            testAuthor.getId(),
            "Экстремальное программирование. Разработка через тестирование");

    assertDoesNotThrow(() -> booksService.createNew(info));
    assertEquals(1, jpaBooksRepository.findAll().size());
  }

  @Test
  public void testAdditionWhenSomethingNull() {
    var tags = new HashSet<String>();
    tags.add("Романы");

    BookCreationInfo infoWithNullAuthor = new BookCreationInfo(
            null,
            "Экстремальное программирование. Разработка через тестирование");
    BookCreationInfo infoWithNullTitle = new BookCreationInfo(
            testAuthor.getId(),
            null);

    Assertions.assertThrows(InvalidBookDataException.class,
            () -> booksService.createNew(infoWithNullAuthor));

    Assertions.assertThrows(InvalidBookDataException.class,
            () -> booksService.createNew(infoWithNullTitle));
  }

  @Test
  public void testAdditionAuthorNotFound() {
    BookCreationInfo info = new BookCreationInfo(
            testAuthor.getId() + 1,
            "Экстремальное программирование. Разработка через тестирование");

    assertThrows(InvalidBookDataException.class, () -> booksService.createNew(info));
    assertEquals(0, jpaBooksRepository.findAll().size());
  }
}
