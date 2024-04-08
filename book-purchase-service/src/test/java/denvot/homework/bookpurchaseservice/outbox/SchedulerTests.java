package denvot.homework.bookpurchaseservice.outbox;

import static org.junit.jupiter.api.Assertions.assertEquals;

import denvot.homework.bookpurchaseservice.DatabaseSuite;
import denvot.homework.bookpurchaseservice.KafkaTestConsumer;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
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
@Import({KafkaAutoConfiguration.class, OutboxScheduler.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class SchedulerTests extends DatabaseSuite {
  @Container
  @ServiceConnection
  public static final KafkaContainer KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

  @Autowired
  private OutboxScheduler scheduler;

  @BeforeAll
  public static void setUp() {
    KAFKA.start();
  }

  @Test
  public void testSchedule() throws InterruptedException {
    scheduler.scheduleMsg("Test");

    var testConsumer =
            new KafkaTestConsumer(KAFKA.getBootstrapServers(), "books-service-group");

    Thread.sleep(12000);

    testConsumer.subscribe(List.of("purchase_status_topic"));
    ConsumerRecords<String, String> records = testConsumer.poll();
    assertEquals(1, records.count());
    records.iterator()
          .forEachRemaining(
                  record -> assertEquals("Test", record.value()));
  }
}
