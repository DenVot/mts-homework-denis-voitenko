package denvot.homework.bookService.outbox;

import jakarta.transaction.Transactional;

public interface OutboxSchedulerBase {
  @Transactional
  void scheduleMsg(String data);
}
