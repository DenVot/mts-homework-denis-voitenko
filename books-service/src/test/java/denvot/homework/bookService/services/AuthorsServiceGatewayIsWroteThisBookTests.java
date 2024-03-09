package denvot.homework.bookService.services;

import denvot.homework.bookService.config.RestTemplateConfiguration;
import org.junit.jupiter.api.*;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@Testcontainers
@SpringBootTest(classes = {AuthorsRegistryServiceGateway.class, RestTemplateConfiguration.class})
class AuthorsServiceGatewayIsWroteThisBookTests {
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
  }

  @Test
  void testIsAuthorWroteThisBook() {
    client.when(request()
            .withPath("/api/authors-registry/is-wrote-this-book"))
            .respond(response()
                    .withStatusCode(200)
                    .withBody("""
                              { "isWrote": true }
                            """)
                    .withContentType(MediaType.APPLICATION_JSON));

    assertTrue(authorsGateway.isAuthorWroteThisBook("Test", "Author", "Test book"));
  }

  @Test
  void testIsAuthorDidntWriteThisBook() {
    client.when(request()
                    .withPath(AuthorsRegistryServiceGateway.IS_AUTHOR_WROTE_THIS_BOOK_ROUTE))
            .respond(response()
                    .withStatusCode(200)
                    .withBody("""
                              { "isWrote": false }
                            """)
                    .withContentType(MediaType.APPLICATION_JSON));

    assertFalse(authorsGateway.isAuthorWroteThisBook("Test", "Author", "Test book"));
  }
}
