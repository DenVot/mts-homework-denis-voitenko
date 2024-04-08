package denvot.homework.bookService.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;

public interface BooksPurchasingManagerBase {
  void createPurchasing(Long id) throws JsonProcessingException, BookNotFoundException;
}
