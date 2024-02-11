package denvot.homework.bookService.controllers.responses;

import com.fasterxml.jackson.annotation.JsonGetter;
import denvot.homework.bookService.data.entities.Book;

public record BookApiEntity(@JsonGetter("newAuthor") String author,
                            @JsonGetter("newTitle") String title,
                            @JsonGetter("newTags") String[] tags) {
  public static BookApiEntity fromBook(Book book) {
    return new BookApiEntity(book.getAuthor(),
            book.getTitle(),
            book.getTags().toArray(new String[0]));
  }
}
