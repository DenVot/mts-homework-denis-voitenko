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

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import({AuthorsService.class})
public class AuthorsServiceCreateNewAuthorTest extends DatabaseSuite {
  @Autowired
  private JpaAuthorsRepository jpaAuthorsRepository;

  @Autowired
  private AuthorsService authorsService;

  @BeforeEach
  public void setUp() {
    jpaAuthorsRepository.deleteAll();
  }

  @Test
  public void testCreateAuthor() {
    Author authorOpt = authorsService.createNew("Firstname", "Lastname");

    assertEquals("Firstname", authorOpt.getFirstName());
    assertEquals("Lastname", authorOpt.getLastName());
  }
}
