package denvot.homework.bookService.controllers.requests;

import jakarta.validation.constraints.NotNull;

public class BookCreationRequest {
  @NotNull
  private final Long authorId;

  @NotNull
  private final String title;

  public BookCreationRequest(Long authorId, String title) {
    this.title = title;
    this.authorId = authorId;
  }

  public Long getAuthorId() {
    return authorId;
  }

  public String getTitle() {
    return title;
  }
}
