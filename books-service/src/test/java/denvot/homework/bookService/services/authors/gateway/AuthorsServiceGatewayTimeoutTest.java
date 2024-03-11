package denvot.homework.bookService.services.authors.gateway;

import denvot.homework.bookService.config.RestTemplateConfiguration;
import denvot.homework.bookService.services.AuthorsRegistryServiceGateway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClientException;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@Testcontainers
@SpringBootTest(classes = {AuthorsRegistryServiceGateway.class, RestTemplateConfiguration.class})
public class AuthorsServiceGatewayTimeoutTest {
  @Container
  public static final MockServerContainer mockServer =
          new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.13.2"));

  @Autowired
  private AuthorsRegistryServiceGateway authorsGateway;

  private static MockServerClient client;

  @BeforeAll
  public static void setup() {
    client = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());
  }

  @AfterEach
  public void resetClient() {
    client.reset();
  }

  @AfterAll
  public static void disposeClient() {
    client.close();
  }

  @DynamicPropertySource
  public static void setProperties(DynamicPropertyRegistry registry) {
    registry.add("authors-registry.service.base.url", mockServer::getEndpoint);
    registry.add("authors-registry.service.timeout", () -> 300);
  }

  @Test
  void testTimeout() {
    client.when(request()
                    .withPath(AuthorsRegistryServiceGateway.IS_AUTHOR_WROTE_THIS_BOOK_ROUTE))
            .respond(request -> {
              Thread.sleep(3000);

              return response().withBody("""
                              { "isWrote": true }
                            """)
                      .withContentType(MediaType.APPLICATION_JSON);
            });

    assertThrows(RestClientException.class, () ->
            authorsGateway.isAuthorWroteThisBook("Test", "Author", "Test book"));
  }
}
