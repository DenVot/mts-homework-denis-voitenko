package denvot.homework.bookService;

import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestHelper {

  public static <T> T assert2xxAndGetBody(ResponseEntity<T> result) {
    assertTrue(result.getStatusCode().is2xxSuccessful());
    assertTrue(result.hasBody());

    var body = result.getBody();
    assertNotNull(body);
    return body;
  }
}
