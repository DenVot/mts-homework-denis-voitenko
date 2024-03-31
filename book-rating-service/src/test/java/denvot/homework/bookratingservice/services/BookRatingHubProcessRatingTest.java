package denvot.homework.bookratingservice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import denvot.homework.bookratingservice.KafkaTestConsumer;
import denvot.homework.bookratingservice.helpers.BookRatingProviderBase;
import java.time.Duration;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecords;
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
      "topic-to-receive-message=some-receive-topic",
      "topic-to-send-message=some-send-topic",
      "spring.kafka.consumer.auto-offset-reset=earliest"
    })
@Import({KafkaAutoConfiguration.class, BookRatingHubProcessRatingTest.ObjectMapperTestConfig.class})
@Testcontainers
public class BookRatingHubProcessRatingTest {
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

  @MockBean
  private BookRatingProviderBase ratingProvider;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Test
  public void testProcess() throws JsonProcessingException {
    when(ratingProvider.rate(0)).thenReturn(5);
    kafkaTemplate.send("some-receive-topic", objectMapper.writeValueAsString(new BookRateRequestMessage(0)));

    await().atMost(Duration.ofSeconds(5))
            .pollDelay(Duration.ofSeconds(1))
            .untilAsserted(() -> Mockito.verify(ratingProvider).rate(0));

    KafkaTestConsumer consumer = new KafkaTestConsumer(KAFKA.getBootstrapServers(), "some-group-id");
    consumer.subscribe(List.of("some-send-topic"));

    ConsumerRecords<String, String> records = consumer.poll();
    assertEquals(1, records.count());
    records.iterator().forEachRemaining(
            record -> {
              try {
                var message = objectMapper.readValue(record.value(), BookRateResponseMessage.class);
                assertEquals(0, message.bookId());
                assertTrue(message.rating() >= 0 && message.rating() <= 10);
              } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
              }
            }
    );
  }
}
