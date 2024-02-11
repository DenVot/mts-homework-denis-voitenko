package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;

public class NewAuthorUpdatingStrategy implements BookUpdatingStrategy {
  private final String newAuthor;

  public NewAuthorUpdatingStrategy(String newAuthor) {
    this.newAuthor = newAuthor;
  }

  @Override
  public void update(Book book) {
    book.setAuthor(newAuthor);
  }
}


