package denvot.homework.bookService.controllers.responses;

import denvot.homework.bookService.data.entities.Book;

public record BookApiEntity(int id, String author, String title, String[] tags) {
  public static BookApiEntity fromBook(Book book) {
    return new BookApiEntity(book.getId().getValue(),
            book.getAuthor(),
            book.getTitle(),
            book.getTags().toArray(new String[0]));
  }
}
