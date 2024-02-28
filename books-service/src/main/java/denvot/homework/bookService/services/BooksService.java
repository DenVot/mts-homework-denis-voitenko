package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.repositories.BooksRepositoryBase;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;
import denvot.homework.bookService.data.repositories.jpa.JpaAuthorsRepository;
import denvot.homework.bookService.exceptions.InvalidBookDataException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;

@Service
public class BooksService implements BooksServiceBase {
  private final BooksRepositoryBase booksRepository;
  private final JpaAuthorsRepository jpaAuthorsRepository;

  public BooksService(BooksRepositoryBase booksRepository, JpaAuthorsRepository jpaAuthorsRepository) {
    this.booksRepository = booksRepository;
    this.jpaAuthorsRepository = jpaAuthorsRepository;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Book createNew(BookCreationInfo creationInfo) throws InvalidBookDataException {
    if (creationInfo.authorId() == null ||
            creationInfo.title() == null) {
      throw new InvalidBookDataException();
    }

    var targetAuthor = jpaAuthorsRepository.findById(creationInfo.authorId());

    if (targetAuthor.isEmpty()) {
      throw new InvalidBookDataException();
    }

    return booksRepository.createBook(targetAuthor.get(), creationInfo.title());
  }

  @Override
  public Optional<Book> findBook(long id) {
    try {
      return Optional.of(booksRepository.findBook(id));
    } catch (BookNotFoundException e) {
      return Optional.empty();
    }
  }

  @Override
  public boolean deleteBook(long id) {
    try {
      booksRepository.deleteBook(id);
      return true;
    } catch (BookNotFoundException e) {
      return false;
    }
  }

  @Override
  public List<Book> getBooksByTag(long tagId) {
    return booksRepository.getByTag(tagId);
  }

  @Override
  public List<Book> getAllBooks() {
    return booksRepository.getAllBooks();
  }


  // TODO string -> long
  @Override
  public Optional<Book> updateBookAuthor(long id, String newAuthorName) {
    //Author author = /*TODO;

    return Optional.empty();
    //return updateBook(id, book -> book.setAuthor(author));
  }

  @Override
  public Optional<Book> updateBookTitle(long id, String newTitle) {
    return updateBook(id, book -> book.setTitle(newTitle));
  }

  // TODO: add/remove tags
  @Override
  public Optional<Book> updateBookTags(long id, Set<String> newTags) {
    return Optional.empty();
  }

  private Optional<Book> updateBook(long id, Consumer<Book> update) {
    var target = findBook(id);
    if (target.isEmpty()) return target;

    var targetBook = target.get();

    update.accept(targetBook);

    return target;
  }
}
