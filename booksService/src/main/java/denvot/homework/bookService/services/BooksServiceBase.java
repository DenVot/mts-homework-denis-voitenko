package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.entities.BookId;
import denvot.homework.bookService.exceptions.InvalidBookDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BooksServiceBase {
  Book createNew(BookCreationInfo creationInfo) throws InvalidBookDataException;

  Optional<Book> findBook(BookId id);

  boolean deleteBook(BookId id);

  Optional<Book> updateBook(BookId id, BookUpdatingStrategy updatingStrategy);

  ArrayList<Book> getBooksByTags(Set<String> tags);

  List<Book> getAllBooks();
}
