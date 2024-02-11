package denvot.homework.bookService.controllers.responses;

import com.fasterxml.jackson.annotation.JsonGetter;
import denvot.homework.bookService.data.entities.Book;

public record BookApiEntity(@JsonGetter("id") int id,
        @JsonGetter("author") String author,
        @JsonGetter("title") String title,
        @JsonGetter("tags") String[] tags) {
  public static BookApiEntity fromBook(Book book) {
    return new BookApiEntity(book.getId().getValue(),
            book.getAuthor(),
            book.getTitle(),
            book.getTags().toArray(new String[0]));
  }
}
