package denvot.homework.bookService.listeners;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;
import denvot.homework.bookService.listeners.entities.PurchaseResponse;
import denvot.homework.bookService.services.BooksServiceBase;
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
        classes = {PurchasingStatusListener.class},
        properties = {
                "topic-to-receive-purchase=some-test-topic",
                "spring.kafka.consumer.auto-offset-reset=earliest"
        }
)
@Import({KafkaAutoConfiguration.class, PurchasingStatusListenerTest.ObjectMapperTestConfig.class})
@Testcontainers
public class PurchasingStatusListenerTest {
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
  private BooksServiceBase booksService;

  @Test
  public void testRatingReceiving() throws JsonProcessingException, BookNotFoundException {
    doNothing().when(booksService).setBookPurchaseStatus(anyLong(), anyBoolean());
    kafkaTemplate.send(
        "some-test-topic", objectMapper.writeValueAsString(new PurchaseResponse(0L, true)));

    await().atMost(Duration.ofSeconds(5))
            .pollDelay(Duration.ofSeconds(1))
            .untilAsserted(() -> Mockito.verify(booksService).setBookPurchaseStatus(anyLong(), anyBoolean()));
  }
}
