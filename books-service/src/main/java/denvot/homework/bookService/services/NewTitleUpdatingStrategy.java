package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;

public class NewTitleUpdatingStrategy implements BookUpdatingStrategy {
  private final String newTitle;

  public NewTitleUpdatingStrategy(String newTitle) {

    this.newTitle = newTitle;
  }

  @Override
  public void update(Book book) {
    book.setTitle(newTitle);
  }
}
