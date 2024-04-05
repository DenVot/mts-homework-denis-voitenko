package denvot.homework.bookratingservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import denvot.homework.bookratingservice.helpers.BookRatingProviderBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class BookRatingHub {
  private final BookRatingProviderBase ratingProvider;
  private final ObjectMapper objectMapper;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final String topicToSendMessage;

  public BookRatingHub(BookRatingProviderBase ratingProvider,
                       ObjectMapper objectMapper,
                       KafkaTemplate<String, String> kafkaTemplate,
                       @Value("${topic-to-send-message}")
                       String topicToSendMessage) {
    this.ratingProvider = ratingProvider;
    this.objectMapper = objectMapper;
    this.kafkaTemplate = kafkaTemplate;
    this.topicToSendMessage = topicToSendMessage;
  }

  @KafkaListener(topics = "${topic-to-receive-message}")
  public void onRateRequested(String message, Acknowledgment ack) throws JsonProcessingException {
    var request = objectMapper.readValue(message, BookRateRequestMessage.class);

    var rate = ratingProvider.rate(request.bookId());
    var responsePayload = objectMapper.writeValueAsString(new BookRateResponseMessage(request.bookId(), rate));

    kafkaTemplate.send(topicToSendMessage, Long.toString(request.bookId()), responsePayload);
    ack.acknowledge();
  }
}
