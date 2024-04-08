package denvot.homework.bookService.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;
import denvot.homework.bookService.listeners.entities.PurchaseResponse;
import denvot.homework.bookService.services.BooksServiceBase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class PurchasingStatusListener {
  private final ObjectMapper objectMapper;
  private final BooksServiceBase booksService;

  public PurchasingStatusListener(ObjectMapper objectMapper, BooksServiceBase booksService) {
    this.objectMapper = objectMapper;
    this.booksService = booksService;
  }

  @KafkaListener(topics = "${topic-to-receive-purchase}")
  public void onNewMsgReceived(String msg, Acknowledgment acknowledgment) throws JsonProcessingException {
    var response = objectMapper.readValue(msg, PurchaseResponse.class);

    try {
      booksService.setBookPurchaseStatus(response.bookId(), response.isSuccess());
    } catch (BookNotFoundException e) {
      // ignore
    }

    acknowledgment.acknowledge();
  }
}
