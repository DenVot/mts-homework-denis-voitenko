package denvot.homework.bookService.services.books;

import denvot.homework.bookService.DatabaseSuite;
import denvot.homework.bookService.data.entities.Author;
import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.repositories.DbBooksRepository;
import denvot.homework.bookService.data.repositories.jpa.JpaAuthorsRepository;
import denvot.homework.bookService.data.repositories.jpa.JpaBooksRepository;
import denvot.homework.bookService.services.AuthorsRegistryServiceGatewayBase;
import denvot.homework.bookService.services.BookRatingHubBase;
import denvot.homework.bookService.services.BooksService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import({BooksService.class, DbBooksRepository.class, AuthorsRegistryServiceGatewayBase.class})
public class BooksServiceFindBooksTests extends DatabaseSuite {
  @Autowired
  private JpaBooksRepository booksRepository;

  @Autowired
  private JpaAuthorsRepository authorsRepository;

  @Autowired
  private BooksService booksService;

  @MockBean
  private AuthorsRegistryServiceGatewayBase authorsRegistry;

  @MockBean
  private BookRatingHubBase ratingHubMock;

  private Book testBook;

  @BeforeEach
  public void setUp() {
    booksRepository.deleteAll();
    authorsRepository.deleteAll();

    var testAuthor = new Author("Test", "Author");
    authorsRepository.save(testAuthor);

    testBook = new Book("Test Book", testAuthor);
    booksRepository.save(testBook);
  }

  @Test
  public void testSimpleFind() {
    var result = booksService.findBook(testBook.getId());

    Assertions.assertTrue(result.isPresent());
    Assertions.assertEquals(testBook.getId(), result.get().getId());
  }

  @Test
  public void testEmptyBook() {
    var result = booksService.findBook(testBook.getId() + 1);

    Assertions.assertFalse(result.isPresent());
  }
}
