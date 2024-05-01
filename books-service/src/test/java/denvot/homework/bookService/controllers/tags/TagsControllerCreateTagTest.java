package denvot.homework.bookService.controllers.tags;

import denvot.homework.bookService.DatabaseSuite;
import denvot.homework.bookService.TestHelper;
import denvot.homework.bookService.controllers.requests.TagCreationRequest;
import denvot.homework.bookService.controllers.responses.TagApiEntity;
import denvot.homework.bookService.data.entities.Role;
import denvot.homework.bookService.data.entities.Tag;
import denvot.homework.bookService.data.entities.User;
import denvot.homework.bookService.data.repositories.jpa.JpaTagsRepository;
import denvot.homework.bookService.data.repositories.jpa.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class TagsControllerCreateTagTest extends DatabaseSuite {
  @Autowired
  private JpaTagsRepository jpaTagsRepository;

  @Autowired
  private TestRestTemplate http;

  private Tag testTag;

  @Autowired
  private JpaUserRepository userRepository;

  @Autowired
  private PasswordEncoder encoder;

  @BeforeEach
  public void authSetup() {
    userRepository.deleteAll();
    userRepository.save(new User("Test", encoder.encode("User"), Set.of(new Role("ADMIN"))));
    http = http.withBasicAuth("Test", "User");
  }

  @BeforeEach
  public void setUp() {
    jpaTagsRepository.deleteAll();
    testTag = jpaTagsRepository.save(new Tag("Test Tag"));
  }

  @Test
  public void testCreateTag() {
    var creationRequest = new TagCreationRequest("Tag");
    var requestEntity = new HttpEntity<>(creationRequest);

    var result = http.postForEntity("/api/tags", requestEntity, TagApiEntity.class);
    TestHelper.assert2xxAndGetBody(result);

    assertEquals(2, jpaTagsRepository.findAll().size());
  }

  @Test
  public void testCreateTagWithNameWhichAlreadyExists() {
    var creationRequest = new TagCreationRequest(testTag.getName());
    var requestEntity = new HttpEntity<>(creationRequest);

    var result = http.postForEntity("/api/tags", requestEntity, TagApiEntity.class);
    assertTrue(result.getStatusCode().is4xxClientError());
  }
}
