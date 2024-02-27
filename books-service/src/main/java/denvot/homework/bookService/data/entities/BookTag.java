package denvot.homework.bookService.data.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "books_tags", schema = "books_service")
public class BookTag {
  @EmbeddedId
  private BookTagId id;

  public BookTagId getId() {
    return id;
  }

  public void setId(BookTagId id) {
    this.id = id;
  }
}