package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;
import denvot.homework.bookService.exceptions.InvalidBookDataException;

import java.util.List;
import java.util.Optional;

public interface BooksServiceBase {
  Book createNew(BookCreationInfo creationInfo) throws InvalidBookDataException;

  Optional<Book> findBook(long id);

  boolean deleteBook(long id);

  List<Book> getBooksByTag(long tagId);

  List<Book> getAllBooks();

  Optional<Book> updateBookAuthor(long id, long newAuthorId);

  Optional<Book> updateBookTitle(long id, String newTitle);

  Optional<Book> addNewTag(Long bookId, Long tagId);

  Optional<Book> removeTag(Long bookId, Long tagId);

  void requestRateBook(Long bookId) throws BookNotFoundException;

  void setBookPurchaseStatus(Long bookId, boolean isSuccess) throws BookNotFoundException;
}
