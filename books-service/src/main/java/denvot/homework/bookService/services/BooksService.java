package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.entities.BookId;
import denvot.homework.bookService.data.repositories.BooksRepositoryBase;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;
import denvot.homework.bookService.exceptions.InvalidBookDataException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;

@Service
public class BooksService implements BooksServiceBase {

  private final BooksRepositoryBase booksRepository;

  public BooksService(BooksRepositoryBase booksRepository) {
    this.booksRepository = booksRepository;
  }

  @Override
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

  @Override
  public Optional<Book> findBook(BookId id) {
    try {
      return Optional.of(booksRepository.findBook(id));
    } catch (BookNotFoundException e) {
      return Optional.empty();
    }
  }

  @Override
  public boolean deleteBook(BookId id) {
    try {
      booksRepository.deleteBook(id);
      return true;
    } catch (BookNotFoundException e) {
      return false;
    }
  }

  @Override
  public ArrayList<Book> getBooksByTags(Set<String> tags) {
    return booksRepository.getByTags(tags);
  }

  @Override
  public List<Book> getAllBooks() {
    return booksRepository.getAllBooks();
  }

  @Override
  public Optional<Book> updateBookAuthor(BookId bookId, String newAuthorName) {
    return updateBook(bookId, book -> book.setAuthor(newAuthorName));
  }

  @Override
  public Optional<Book> updateBookTitle(BookId bookId, String newTitle) {
    return updateBook(bookId, book -> book.setTitle(newTitle));
  }

  @Override
  public Optional<Book> updateBookTags(BookId bookId, Set<String> newTags) {
    return updateBook(bookId, book -> book.setTags(newTags));
  }

  private Optional<Book> updateBook(BookId bookId, Consumer<Book> update) {
    var target = findBook(bookId);
    if (target.isEmpty()) return target;

    var targetBook = target.get();

    update.accept(targetBook);

    return target;
  }
}
