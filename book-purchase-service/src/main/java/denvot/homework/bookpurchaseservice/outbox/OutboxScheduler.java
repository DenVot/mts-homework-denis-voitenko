package denvot.homework.bookpurchaseservice.outbox;

import denvot.homework.bookpurchaseservice.data.entities.OutboxRecord;
import denvot.homework.bookpurchaseservice.data.repositories.OutboxRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OutboxScheduler implements OutboxSchedulerBase {
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final String topic;
  private final OutboxRepository outboxRepository;

  public OutboxScheduler(KafkaTemplate<String, String> kafkaTemplate,
                         @Value("${purchase-status-topic}")
                         String topic,
                         OutboxRepository outboxRepository) {
    this.kafkaTemplate = kafkaTemplate;
    this.topic = topic;
    this.outboxRepository = outboxRepository;
  }

  @Transactional
  @Scheduled(fixedDelay = 10000)
  public void processOutbox() {
    List<OutboxRecord> result = outboxRepository.findAll();

    for (OutboxRecord outboxRecord : result) {
      kafkaTemplate.send(topic, outboxRecord.getData());
    }

    outboxRepository.deleteAll(result);
  }

  @Override
  @Transactional
  public void scheduleMsg(String data) {
    outboxRepository.save(new OutboxRecord(data));
  }
}
