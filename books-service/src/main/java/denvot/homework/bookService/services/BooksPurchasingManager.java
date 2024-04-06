package denvot.homework.bookService.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import denvot.homework.bookService.data.entities.PurchasingStatus;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;
import denvot.homework.bookService.data.repositories.jpa.JpaBooksRepository;
import denvot.homework.bookService.listeners.entities.PurchaseRequest;
import denvot.homework.bookService.outbox.OutboxSchedulerBase;
import org.springframework.stereotype.Service;

@Service
public class BooksPurchasingManager implements BooksPurchasingManagerBase {
  private final OutboxSchedulerBase scheduler;
  private final ObjectMapper objectMapper;
  private final JpaBooksRepository booksRepository;

  public BooksPurchasingManager(OutboxSchedulerBase scheduler, ObjectMapper objectMapper, JpaBooksRepository booksRepository) {
    this.scheduler = scheduler;
    this.objectMapper = objectMapper;
    this.booksRepository = booksRepository;
  }

  @Override
  public void createPurchasing(Long id) throws JsonProcessingException, BookNotFoundException {
    var book = booksRepository.findById(id).orElseThrow(BookNotFoundException::new);

    if (book.getPurchasingStatus() == PurchasingStatus.IN_PROCESS ||
            book.getPurchasingStatus() == PurchasingStatus.DONE) {
      return;
    }

    scheduler.scheduleMsg(objectMapper.writeValueAsString(new PurchaseRequest(id, 100)));
  }
}
