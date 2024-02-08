package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;

import java.util.Set;

public class NewTagsUpdatingStrategy implements BookUpdatingStrategy {
  private final Set<String> newTags;

  public NewTagsUpdatingStrategy(Set<String> newTags) {
    this.newTags = newTags;
  }

  @Override
  public void update(Book book) {
    book.setTags(newTags);
  }
}
