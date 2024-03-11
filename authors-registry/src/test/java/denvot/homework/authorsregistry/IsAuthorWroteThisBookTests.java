package denvot.homework.authorsregistry;

import denvot.homework.authorsregistry.controllers.reponses.IsWroteStatusResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IsAuthorWroteThisBookTests {
  @Autowired
  private TestRestTemplate http;

  @Test
  public void testIsWrote() {
    var result = http.getForEntity(
            "/api/authors-registry/is-wrote-this-book?firstName={firstName}&lastName={lastName}&bookName={bookName}",
            IsWroteStatusResponse.class,
            Map.of("firstName", "Кент",
                    "lastName", "Бек",
                    "bookName", "TDD"));

    assertTrue(result.getStatusCode().is2xxSuccessful());
    assertTrue(result.hasBody());
    assertNotNull(result.getBody());
    assertTrue(result.getBody().isWrote());
  }

  @Test
  public void testIsNotWrote() {
    var result = http.getForEntity(
            "/api/authors-registry/is-wrote-this-book?firstName={firstName}&lastName={lastName}&bookName={bookName}",
            IsWroteStatusResponse.class,
            Map.of("firstName", "Кент",
                    "lastName", "Бек",
                    "bookName", "TDD2"));

    assertTrue(result.getStatusCode().is2xxSuccessful());
    assertTrue(result.hasBody());
    assertNotNull(result.getBody());
    assertFalse(result.getBody().isWrote());
  }
}
