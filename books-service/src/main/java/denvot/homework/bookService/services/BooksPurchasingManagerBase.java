package denvot.homework.bookService.services;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface BooksPurchasingManagerBase {
  void createPurchasing(Long id) throws JsonProcessingException;
}
