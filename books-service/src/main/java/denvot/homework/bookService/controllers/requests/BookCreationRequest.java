package denvot.homework.bookService.controllers.requests;

import jakarta.validation.constraints.NotNull;

public class BookCreationRequest {
  @NotNull
  private final String author;

  @NotNull
  private final String title;

  @NotNull
  private final String[] tags;

  public BookCreationRequest(String author, String title, String[] tags) {

    this.author = author;
    this.title = title;
    this.tags = tags;
  }

  public String getAuthor() {
    return author;
  }

  public String getTitle() {
    return title;
  }

  public String[] getTags() {
    return tags;
  }
}
