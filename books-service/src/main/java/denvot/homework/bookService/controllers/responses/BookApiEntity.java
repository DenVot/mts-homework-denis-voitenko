package denvot.homework.bookService.controllers.responses;

import denvot.homework.bookService.data.entities.Book;

import java.util.List;
import java.util.stream.Collectors;

public record BookApiEntity(long id, AuthorApiEntity author, String title, List<TagApiEntity> tags) {
  public static BookApiEntity fromBook(Book book) {
    return new BookApiEntity(book.getId(),
            AuthorApiEntity.fromAuthor(book.getAuthor()),
            book.getTitle(),
            book.getTags().stream().map(TagApiEntity::fromTag).collect(Collectors.toList()));
  }
}
