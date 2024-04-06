package denvot.homework.bookService.services.books;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import denvot.homework.bookService.DatabaseSuite;
import denvot.homework.bookService.outbox.OutboxSchedulerBase;
import denvot.homework.bookService.services.BooksPurchasingManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = {BooksPurchasingManager.class})
@Import({BooksPurchasingManagerTests.ObjectMapperTestConfig.class})
public class BooksPurchasingManagerTests extends DatabaseSuite {
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
  private OutboxSchedulerBase outboxScheduler;

  @Autowired
  private BooksPurchasingManager booksPurchasingManager;

  @Test
  public void testSend() throws JsonProcessingException {
    booksPurchasingManager.createPurchasing(0L);
    verify(outboxScheduler).scheduleMsg(any());
  }
}
