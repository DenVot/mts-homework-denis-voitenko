package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;

public class StartUpdatingStrategy implements BookUpdatingStrategy {
  private final Iterable<BookUpdatingStrategy> strategies;

  public StartUpdatingStrategy(Iterable<BookUpdatingStrategy> strategies) {
    this.strategies = strategies;
  }

  @Override
  public void update(Book book) {
    for (BookUpdatingStrategy strategy : strategies) {
      strategy.update(book);
    }
  }
}
