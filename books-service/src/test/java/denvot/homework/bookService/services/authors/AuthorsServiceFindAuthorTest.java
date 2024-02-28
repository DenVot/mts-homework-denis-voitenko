package denvot.homework.bookService.services.authors;

import denvot.homework.bookService.DatabaseSuite;
import denvot.homework.bookService.data.entities.Author;
import denvot.homework.bookService.data.repositories.jpa.JpaAuthorsRepository;
import denvot.homework.bookService.services.AuthorsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import({AuthorsService.class})
public class AuthorsServiceFindAuthorTest extends DatabaseSuite {
  @Autowired
  private JpaAuthorsRepository jpaAuthorsRepository;

  @Autowired
  private AuthorsService authorsService;

  private Author testAuthor;

  @BeforeEach
  public void setUp() {
    jpaAuthorsRepository.deleteAll();
    testAuthor = jpaAuthorsRepository.save(new Author("Test", "Author"));
  }

  @Test
  public void testFindAuthor() {
    Optional<Author> result = authorsService.findAuthor(testAuthor.getId());

    assertTrue(result.isPresent());
    assertEquals(testAuthor.getId(), result.get().getId());
  }

  @Test
  public void testFindAuthorNoAuthor() {
    Optional<Author> result = authorsService.findAuthor(testAuthor.getId() + 1);

    assertTrue(result.isEmpty());
  }
}
