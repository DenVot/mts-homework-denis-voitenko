package denvot.homework.bookService.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BookUpdateBuilder {
  private final List<BookUpdatingStrategy> strategies = new ArrayList<>();

  public BookUpdateBuilder withAuthor(String newAuthor) {
    strategies.add(new NewAuthorUpdatingStrategy(newAuthor));
    return this;
  }

  public BookUpdateBuilder withTitle(String newTitle) {
    strategies.add(new NewTitleUpdatingStrategy(newTitle));
    return this;
  }

  public BookUpdateBuilder withNewTags(Set<String> tags) {
    strategies.add(new NewTagsUpdatingStrategy(tags));
    return this;
  }

  public BookUpdatingStrategy build() {
    return new StartUpdatingStrategy(strategies);
  }
}
