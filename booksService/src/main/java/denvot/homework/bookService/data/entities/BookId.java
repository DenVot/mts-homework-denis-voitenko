package denvot.homework.bookService.data.entities;

import java.util.Objects;

public final class BookId {
  private final int id;

  public BookId(int value) {
    this.id = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BookId bookId = (BookId) o;
    return id == bookId.id;
  }

  public int getValue() {
    return id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
