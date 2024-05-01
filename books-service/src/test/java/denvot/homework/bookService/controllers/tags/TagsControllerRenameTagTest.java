package denvot.homework.bookService.controllers.tags;

import denvot.homework.bookService.DatabaseSuite;
import denvot.homework.bookService.TestHelper;
import denvot.homework.bookService.controllers.requests.TagUpdateRequest;
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
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class TagsControllerRenameTagTest extends DatabaseSuite {
  @Autowired
  private JpaTagsRepository tagRepository;

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
    tagRepository.deleteAll();

    testTag = tagRepository.save(new Tag("Test"));
  }

  @Test
  public void testRenameTag() {
    var updateRequest = new TagUpdateRequest("Updated Test");
    var updateRequestEntity = new HttpEntity<>(updateRequest);
    var response = http.exchange("/api/tags/{id}", HttpMethod.PATCH, updateRequestEntity, TagApiEntity.class, Map.of("id", testTag.getId()));
    var body = TestHelper.assert2xxAndGetBody(response);

    assertEquals(testTag.getId(), body.id());
    assertEquals("Updated Test", body.name());
  }

  @Test
  public void testRenameTagNotFound() {
    var updateRequest = new TagUpdateRequest("Updated Test");
    var updateRequestEntity = new HttpEntity<>(updateRequest);
    var response = http.exchange("/api/tags/{id}", HttpMethod.PATCH, updateRequestEntity, TagApiEntity.class, Map.of("id", testTag.getId() + 1));
    assertTrue(response.getStatusCode().is4xxClientError());
  }

  @Test
  public void testRenameTagAlreadyExistsWithThisName() {
    tagRepository.save(new Tag("Updated Test"));

    var updateRequest = new TagUpdateRequest("Updated Test");
    var updateRequestEntity = new HttpEntity<>(updateRequest);
    var response = http.exchange("/api/tags/{id}", HttpMethod.PATCH, updateRequestEntity, TagApiEntity.class, Map.of("id", testTag.getId()));
    assertTrue(response.getStatusCode().is4xxClientError());
  }
}
