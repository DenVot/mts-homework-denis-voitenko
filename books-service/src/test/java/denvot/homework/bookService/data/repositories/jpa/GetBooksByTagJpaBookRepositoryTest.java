package denvot.homework.bookService.data.repositories.jpa;

import denvot.homework.bookService.DatabaseSuite;
import denvot.homework.bookService.data.entities.Author;
import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.entities.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class GetBooksByTagJpaBookRepositoryTest extends DatabaseSuite {

  @Autowired
  private JpaBooksRepository booksRepo;

  @Autowired
  private JpaAuthorsRepository authorsRepo;

  @Autowired
  private JpaTagsRepository tagsRepo;

  @BeforeEach
  public void setUp() {
    tagsRepo.deleteAll();
  }

  @Test
  public void testFilterByTag() {
    var tag = new Tag("testTag");
    var author = new Author("Кент", "Бек");

    tagsRepo.save(tag);
    authorsRepo.save(author);

    var book = new Book("TDD", author);

    booksRepo.save(book);

    book.assignTag(tag);

    booksRepo.save(book);

    assertEquals(1, booksRepo.findBooksByTag(tag.getId()).size());
  }

  @Test
  public void testFilterByTagNothing() {
    var tag = new Tag("testTag");
    var author = new Author("Кент", "Бек");

    tagsRepo.save(tag);
    authorsRepo.save(author);

    var book = new Book("TDD", author);

    booksRepo.save(book);

    book.assignTag(tag);

    booksRepo.save(book);

    assertEquals(0, booksRepo.findBooksByTag(0L).size());
  }
}