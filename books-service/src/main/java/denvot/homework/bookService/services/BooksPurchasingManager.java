package denvot.homework.bookService.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import denvot.homework.bookService.listeners.entities.PurchaseRequest;
import denvot.homework.bookService.outbox.OutboxSchedulerBase;
import org.springframework.stereotype.Service;

@Service
public class BooksPurchasingManager implements BooksPurchasingManagerBase {
  private final OutboxSchedulerBase scheduler;
  private final ObjectMapper objectMapper;

  public BooksPurchasingManager(OutboxSchedulerBase scheduler, ObjectMapper objectMapper) {
    this.scheduler = scheduler;
    this.objectMapper = objectMapper;
  }

  @Override
  public void createPurchasing(Long id) throws JsonProcessingException {
    scheduler.scheduleMsg(objectMapper.writeValueAsString(new PurchaseRequest(id, 100)));
  }
}
