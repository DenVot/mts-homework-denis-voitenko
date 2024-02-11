package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;

public interface BookUpdatingStrategy {
  void update(Book book);
}
