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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import(TagsService.class)
public class TagsServiceCreateTagTest extends DatabaseSuite {
  @Autowired
  private JpaTagsRepository tagsRepository;

  @Autowired
  private TagsService tagsService;

  @BeforeEach
  public void setUp() {
    tagsRepository.deleteAll();
  }

  @Test
  public void testCreateTag() throws TagAlreadyExistsException {
    Tag tag = tagsService.createNew("Tag");

    assertEquals(1, tagsRepository.findAll().size());
    assertEquals(tagsRepository.findAll().get(0).getId(), tag.getId());
  }

  @Test
  public void testCreateTagWhichAlreadyExists() {
    tagsRepository.save(new Tag("Tag"));

    assertThrows(TagAlreadyExistsException.class, () -> tagsService.createNew("Tag"));
  }
}
