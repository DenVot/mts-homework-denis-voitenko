package denvot.homework.bookService.services.books.rating;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import denvot.homework.bookService.KafkaTestConsumer;
import denvot.homework.bookService.data.repositories.BooksRepositoryBase;
import denvot.homework.bookService.services.BookRatingHub;
import denvot.homework.bookService.services.RateBookMessage;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(
        classes = {BookRatingHub.class},
        properties = {"topic-to-send-message=some-test-topic"}
)
@Import({KafkaAutoConfiguration.class, BookRatingHubKafkaTests.ObjectMapperTestConfig.class})
@Testcontainers
public class BookRatingHubKafkaTests {
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
  private BookRatingHub ratingHub;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private BooksRepositoryBase repository;

  @Test
  public void testSendMsgToKafka() {
    assertDoesNotThrow(() -> ratingHub.requestRating(1551L));

    KafkaTestConsumer consumer = new KafkaTestConsumer(KAFKA.getBootstrapServers(), "some-group-id");
    consumer.subscribe(List.of("some-test-topic"));

    ConsumerRecords<String, String> records = consumer.poll();
    assertEquals(1, records.count());
    records.iterator().forEachRemaining(
      record -> {
        try {
          var message = objectMapper.readValue(record.value(), RateBookMessage.class);
          assertEquals(new RateBookMessage(1551L), message);
        } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
        }
      }
    );
  }
}
