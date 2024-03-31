package denvot.homework.bookService.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import denvot.homework.bookService.data.repositories.BooksRepositoryBase;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookRatingHub implements BookRatingHubBase {
  private final ObjectMapper objectMapper;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final String requestTopic;
  private final BooksRepositoryBase booksRepositoryBase;

  public BookRatingHub(ObjectMapper objectMapper,
                       KafkaTemplate<String, String> kafkaTemplate,
                       @Value("${topic-to-send-message}")
                       String requestTopic,
                       BooksRepositoryBase booksRepositoryBase) {
    this.objectMapper = objectMapper;
    this.kafkaTemplate = kafkaTemplate;
    this.requestTopic = requestTopic;
    this.booksRepositoryBase = booksRepositoryBase;
  }

  @Override
  public void requestRating(long id) {
    try {
      var result = kafkaTemplate.send(requestTopic, Long.toString(id), objectMapper.writeValueAsString(new RateBookMessage(id)));

      result.get(2, TimeUnit.SECONDS);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Unexpected thread interruption", e);
    } catch (ExecutionException e) {
      throw new RuntimeException("Couldn't send message to Kafka", e);
    } catch (TimeoutException e) {
      throw new RuntimeException("Couldn't send message to Kafka due to timeout", e);
    }
  }

  @KafkaListener(topics = {"${topic-to-receive-message}"})
  @Transactional
  public void onKafkaMessageReceived(String message, Acknowledgment ack)
      throws JsonProcessingException {
    var response = objectMapper.readValue(message, BookRateResponseMessage.class);
    try {
      var targetBook = booksRepositoryBase.findBook(response.bookId());

      targetBook.setRating(response.rating());
      ack.acknowledge();
    } catch (BookNotFoundException e) {
      // Ignore
    }
  }
}
