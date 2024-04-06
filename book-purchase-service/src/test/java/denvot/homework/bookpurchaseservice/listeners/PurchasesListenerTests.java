package denvot.homework.bookpurchaseservice.listeners;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import denvot.homework.bookpurchaseservice.DatabaseSuite;
import denvot.homework.bookpurchaseservice.KafkaTestConsumer;
import denvot.homework.bookpurchaseservice.exceptions.NotEnoughMoneyException;
import denvot.homework.bookpurchaseservice.listeners.entities.PurchaseRequest;
import denvot.homework.bookpurchaseservice.listeners.entities.PurchaseResponse;
import denvot.homework.bookpurchaseservice.outbox.OutboxScheduler;
import denvot.homework.bookpurchaseservice.services.WalletServiceBase;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@DataJpaTest(properties = {
                "purchase-request-topic = purchase_request_topic",
                "purchase-status-topic = purchase_status_topic",
                "spring.kafka.consumer.auto-offset-reset=earliest"
        })
@Import({KafkaAutoConfiguration.class, PurchasesListenerTests.ObjectMapperTestConfig.class, PurchasesListener.class, OutboxScheduler.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class PurchasesListenerTests extends DatabaseSuite {
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
  private WalletServiceBase walletService;

  @BeforeAll
  public static void setUp() {
    KAFKA.start();
  }

  @Test
  public void testPurchase() throws JsonProcessingException, InterruptedException {
    kafkaTemplate.send("purchase_request_topic", Long.toString(0),
            objectMapper.writeValueAsString(new PurchaseRequest(0L, 50)));

    Thread.sleep(15000);

    var testConsumer =
            new KafkaTestConsumer(KAFKA.getBootstrapServers(), "books-service-group");
    
    testConsumer.subscribe(List.of("purchase_status_topic"));
    ConsumerRecords<String, String> records = testConsumer.poll();
    assertEquals(1, records.count());
    records
        .iterator()
        .forEachRemaining(
            record -> {
              try {
                var message = objectMapper.readValue(record.value(), PurchaseResponse.class);
                assertEquals(new PurchaseResponse(0L, true), message);
              } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
              }
            });
  }

  @Test
  public void testPurchaseNotEnoughMoney() throws JsonProcessingException, InterruptedException {
    doThrow(new NotEnoughMoneyException()).when(walletService).pay(anyInt());
    kafkaTemplate.send("purchase_request_topic", Long.toString(0),
            objectMapper.writeValueAsString(new PurchaseRequest(0L, 50)));

    Thread.sleep(15000);

    var testConsumer =
            new KafkaTestConsumer(KAFKA.getBootstrapServers(), "books-service-group");

    testConsumer.subscribe(List.of("purchase_status_topic"));
    ConsumerRecords<String, String> records = testConsumer.poll();
    assertEquals(1, records.count());
    records
            .iterator()
            .forEachRemaining(
                    record -> {
                      try {
                        var message = objectMapper.readValue(record.value(), PurchaseResponse.class);
                        assertEquals(new PurchaseResponse(0L, false), message);
                      } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                      }
                    });
  }
}
