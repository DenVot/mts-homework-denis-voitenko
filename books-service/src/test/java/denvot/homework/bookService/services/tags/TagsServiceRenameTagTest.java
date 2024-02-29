package denvot.homework.bookService.services.tags;

import denvot.homework.bookService.DatabaseSuite;
import denvot.homework.bookService.data.entities.Tag;
import denvot.homework.bookService.data.repositories.jpa.JpaTagsRepository;
import denvot.homework.bookService.exceptions.TagAlreadyExistsException;
import denvot.homework.bookService.services.TagsService;
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

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import(TagsService.class)
public class TagsServiceRenameTagTest  extends DatabaseSuite {
  @Autowired
  private JpaTagsRepository tagsRepository;

  @Autowired
  private TagsService tagsService;

  private Tag testTag;

  @BeforeEach
  public void setUp() {
    tagsRepository.deleteAll();
    testTag = tagsRepository.save(new Tag("Test"));
  }

  @Test
  public void testRename() throws TagAlreadyExistsException {
    Optional<Tag> target = tagsService.rename(testTag.getId(), "Renamed");

    assertTrue(target.isPresent());
    assertEquals(testTag.getId(), target.get().getId());
    assertEquals("Renamed", target.get().getName());
  }

  @Test
  public void testRenameAlreadyExists() {
    var anotherTag = new Tag("Another tag");
    tagsRepository.save(anotherTag);

    assertThrows(TagAlreadyExistsException.class, () -> tagsService.rename(anotherTag.getId(), "Test"));
  }
}
