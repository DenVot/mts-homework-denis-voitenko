package denvot.homework.bookService.controllers.tags;

import denvot.homework.bookService.DatabaseSuite;
import denvot.homework.bookService.data.entities.Tag;
import denvot.homework.bookService.data.repositories.jpa.JpaTagsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class TagsControllerDeleteTagTest extends DatabaseSuite {
  @Autowired
  private JpaTagsRepository tagRepository;

  @Autowired
  private TestRestTemplate http;

  private Tag testTag;

  @BeforeEach
  public void setUp() {
    tagRepository.deleteAll();

    testTag = tagRepository.save(new Tag("Test"));
  }

  @Test
  public void createTag() {
    http.delete("/api/tags/{id}", Map.of("id", testTag.getId()));

    assertEquals(0, tagRepository.findAll().size());
  }
}
