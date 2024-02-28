package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.exceptions.InvalidBookDataException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BooksServiceBase {
  Book createNew(BookCreationInfo creationInfo) throws InvalidBookDataException;

  Optional<Book> findBook(long id);

  boolean deleteBook(long id);

  List<Book> getBooksByTag(long tagId);

  List<Book> getAllBooks();

  Optional<Book> updateBookAuthor(long id, long newAuthorId);

  Optional<Book> updateBookTitle(long id, String newTitle);

  Optional<Book> updateBookTags(long id, Set<String> newTags);

  Optional<Book> addNewTag(Long bookId, Long tagId);

  Optional<Book> removeTag(Long bookId, Long tagId);
}
