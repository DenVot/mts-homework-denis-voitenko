package denvot.homework.bookService.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthorsRegistryServiceGateway implements AuthorsRegistryServiceGatewayBase {
  private final RestTemplate http;
  public static final String IS_AUTHOR_WROTE_THIS_BOOK_ROUTE = "/api/authors-registry/is-wrote-this-book";

  public AuthorsRegistryServiceGateway(RestTemplate http) {
    this.http = http;
  }

  public record IsAuthorWroteThisBookResponse(boolean isWrote) {

  }

  @Override
  public boolean isAuthorWroteThisBook(String firstName, String lastName, String bookName) {
    var result = http.getForEntity(IS_AUTHOR_WROTE_THIS_BOOK_ROUTE, IsAuthorWroteThisBookResponse.class);

    if (!result.getStatusCode().is2xxSuccessful() || !result.hasBody() || result.getBody() == null) {
      throw new RuntimeException();
    }

    return result.getBody().isWrote();
  }
}
