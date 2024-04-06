package denvot.homework.bookpurchaseservice.outbox;

import jakarta.transaction.Transactional;

public interface OutboxSchedulerBase {
  @Transactional
  void scheduleMsg(String data);
}
