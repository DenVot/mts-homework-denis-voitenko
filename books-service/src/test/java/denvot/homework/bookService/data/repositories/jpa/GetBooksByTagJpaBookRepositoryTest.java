package denvot.homework.bookService.data.repositories.jpa;

import denvot.homework.bookService.DatabaseSuite;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class GetBooksByTagJpaBookRepositoryTest extends DatabaseSuite {

  /*@Autowired
  private JpaBooksRepository booksRepo;

  @Autowired
  private JpaAuthorsRepository authorsRepo;

  @Autowired
  private JpaTagsRepository tagsRepo;*/


  @Test
  public void testFilterByTag() {
    /*var tag = new Tag("testTag");
    var author = new Author("Кент", "Бек");

    tagsRepo.save(tag);
    authorsRepo.save(author);

    var book = new Book("TDD");
    author.assignNewBook(book);

    authorsRepo.save(author);
    booksRepo.save(book);

    assertEquals(1, booksRepo.findBooksByTag(tag.getId()).size());*/
  }
}