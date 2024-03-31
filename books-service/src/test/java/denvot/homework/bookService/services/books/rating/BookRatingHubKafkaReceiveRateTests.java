package denvot.homework.bookService.services.books.rating;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import denvot.homework.bookService.data.entities.Author;
import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.repositories.BooksRepositoryBase;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;
import denvot.homework.bookService.services.BookRateResponseMessage;
import denvot.homework.bookService.services.BookRatingHub;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(
        classes = {BookRatingHub.class},
        properties = {
                "topic-to-receive-message=some-test-topic",
                "spring.kafka.consumer.auto-offset-reset=earliest"
        }
)
@Import({KafkaAutoConfiguration.class, BookRatingHubKafkaReceiveRateTests.ObjectMapperTestConfig.class})
@Testcontainers
public class BookRatingHubKafkaReceiveRateTests {
  @TestConfiguration
  static class ObjectMapperTestConfig {
    @Bean
    public ObjectMapper objectMapper() {
      return new ObjectMapper();
    }
  }

  @Container
  @ServiceConnection
  public static final KafkaContainer KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private BooksRepositoryBase booksRepository;

  @Test
  public void testRatingReceiving() throws JsonProcessingException, BookNotFoundException {
    when(booksRepository.findBook(anyLong())).thenReturn(new Book("Test", new Author("Test", "Author")));
    kafkaTemplate.send("some-test-topic", objectMapper.writeValueAsString(new BookRateResponseMessage(0, 10)));

    await().atMost(Duration.ofSeconds(5))
            .pollDelay(Duration.ofSeconds(1))
            .untilAsserted(() -> Mockito.verify(booksRepository).findBook(anyLong()));
  }
}
