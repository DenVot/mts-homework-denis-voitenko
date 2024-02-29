package denvot.homework.bookService.controllers.authors;

import denvot.homework.bookService.DatabaseSuite;
import denvot.homework.bookService.controllers.requests.AuthorCreationRequest;
import denvot.homework.bookService.controllers.responses.AuthorApiEntity;
import denvot.homework.bookService.data.repositories.jpa.JpaAuthorsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class AuthorControllerCreateAuthorTest extends DatabaseSuite {
  @Autowired
  private JpaAuthorsRepository authorsRepository;

  @Autowired
  private TestRestTemplate http;

  @BeforeEach
  public void setUp() {
    authorsRepository.deleteAll();
  }

  @Test
  public void createAuthor() {
    var creationRequest = new AuthorCreationRequest("Кент", "Бек");

    var result = http.postForEntity("/api/authors", creationRequest, AuthorApiEntity.class);

    assertTrue(result.getStatusCode().is2xxSuccessful());
    assertTrue(result.hasBody());

    var body = result.getBody();

    assertNotNull(body);
    assertEquals("Кент", body.firstName());
    assertEquals("Бек", body.lastName());
    assertEquals(1, authorsRepository.findAll().size());
    assertEquals(authorsRepository.findAll().get(0).getId(), body.id());
  }
}
