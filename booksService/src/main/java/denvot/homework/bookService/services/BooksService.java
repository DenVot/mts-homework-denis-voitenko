package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.entities.BookId;
import denvot.homework.bookService.data.repositories.BooksRepository;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;
import denvot.homework.bookService.exceptions.InvalidBookDataException;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

public class BooksService {

  private final BooksRepository booksRepository;

  public BooksService(BooksRepository booksRepository) {
    this.booksRepository = booksRepository;
  }

  public Book createNew(BookCreationInfo creationInfo) throws InvalidBookDataException {
    if (creationInfo.author() == null ||
            creationInfo.title() == null ||
            creationInfo.tags() == null) {
      throw new InvalidBookDataException();
    }

    return booksRepository.createBook(creationInfo.author(),
            creationInfo.title(),
            creationInfo.tags());
  }

  public Optional<Book> findBook(BookId id) {
    try {
      return Optional.of(booksRepository.findBook(id));
    } catch (BookNotFoundException e) {
      return Optional.empty();
    }
  }

  public boolean deleteBook(BookId id) {
    try {
      booksRepository.deleteBook(id);
      return true;
    } catch (BookNotFoundException e) {
      return false;
    }
  }

  public Optional<Book> updateBook(BookId id, BookUpdatingStrategy updatingStrategy) {
    var target = findBook(id);
    if (target.isEmpty()) return target;

    var targetBook = target.get();

    updatingStrategy.update(targetBook);

    return target;
  }

  public ArrayList<Book> getBooksByTags(Set<String> tags) {
    return booksRepository.getByTags(tags);
  }
}