package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.exceptions.InvalidBookDataException;

import java.util.*;

public interface BooksServiceBase {
  Book createNew(BookCreationInfo creationInfo) throws InvalidBookDataException;

  Optional<Book> findBook(long id);

  boolean deleteBook(long id);

  ArrayList<Book> getBooksByTags(Set<String> tags);

  List<Book> getAllBooks();

  Optional<Book> updateBookAuthor(long id, String newAuthorName);

  Optional<Book> updateBookTitle(long id, String newTitle);

  Optional<Book> updateBookTags(long id, Set<String> newTags);
}
