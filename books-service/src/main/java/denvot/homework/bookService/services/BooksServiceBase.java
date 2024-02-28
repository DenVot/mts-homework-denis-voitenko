package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.entities.BookId;
import denvot.homework.bookService.exceptions.InvalidBookDataException;

import java.util.*;

public interface BooksServiceBase {
  Book createNew(BookCreationInfo creationInfo) throws InvalidBookDataException;

  Optional<Book> findBook(BookId id);

  boolean deleteBook(BookId id);

  ArrayList<Book> getBooksByTags(Set<String> tags);

  List<Book> getAllBooks();

  Optional<Book> updateBookAuthor(BookId bookId, String newAuthorName);

  Optional<Book> updateBookTitle(BookId bookId, String newTitle);

  Optional<Book> updateBookTags(BookId bookId, Set<String> newTags);
}
