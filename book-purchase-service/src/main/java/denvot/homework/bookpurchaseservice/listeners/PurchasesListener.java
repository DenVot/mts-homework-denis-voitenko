package denvot.homework.bookpurchaseservice.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import denvot.homework.bookpurchaseservice.exceptions.NotEnoughMoneyException;
import denvot.homework.bookpurchaseservice.listeners.entities.PurchaseRequest;
import denvot.homework.bookpurchaseservice.listeners.entities.PurchaseResponse;
import denvot.homework.bookpurchaseservice.outbox.OutboxSchedulerBase;
import denvot.homework.bookpurchaseservice.services.WalletServiceBase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class PurchasesListener {
  private final WalletServiceBase walletService;
  private final ObjectMapper objectMapper;
  private final OutboxSchedulerBase scheduler;

  public PurchasesListener(WalletServiceBase walletService, ObjectMapper objectMapper, OutboxSchedulerBase scheduler) {
    this.walletService = walletService;
    this.objectMapper = objectMapper;
    this.scheduler = scheduler;
  }

  @KafkaListener(topics = {"${purchase-request-topic}"})
  public void onNewPurchaseRequest(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
    var request = objectMapper.readValue(message, PurchaseRequest.class);
    var isSuccess = true;

    try {
      walletService.pay(request.amount());
    } catch (NotEnoughMoneyException e) {
      isSuccess = false;
    }

    scheduler.scheduleMsg(objectMapper.writeValueAsString(new PurchaseResponse(request.bookId(), isSuccess)));
    acknowledgment.acknowledge();
  }
}
