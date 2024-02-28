package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.repositories.BooksRepositoryBase;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;
import denvot.homework.bookService.data.repositories.jpa.JpaAuthorsRepository;
import denvot.homework.bookService.data.repositories.jpa.JpaTagsRepository;
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
  private final JpaTagsRepository jpaTagsRepository;

  public BooksService(BooksRepositoryBase booksRepository, JpaAuthorsRepository jpaAuthorsRepository,
                      JpaTagsRepository jpaTagsRepository) {
    this.booksRepository = booksRepository;
    this.jpaAuthorsRepository = jpaAuthorsRepository;
    this.jpaTagsRepository = jpaTagsRepository;
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
  @Transactional(propagation = Propagation.REQUIRED)
  public Optional<Book> findBook(long id) {
    try {
      return Optional.of(booksRepository.findBook(id));
    } catch (BookNotFoundException e) {
      return Optional.empty();
    }
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public boolean deleteBook(long id) {
    try {
      booksRepository.deleteBook(id);
      return true;
    } catch (BookNotFoundException e) {
      return false;
    }
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public List<Book> getBooksByTag(long tagId) {
    return booksRepository.getByTag(tagId);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public List<Book> getAllBooks() {
    return booksRepository.getAllBooks();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Optional<Book> updateBookAuthor(long id, long newAuthorId) {
    var targetAuthorOpt = jpaAuthorsRepository.findById(newAuthorId);

    return targetAuthorOpt.flatMap(author -> updateBook(id, book -> book.setAuthor(author)));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Optional<Book> updateBookTitle(long id, String newTitle) {
    return updateBook(id, book -> book.setTitle(newTitle));
  }

  // TODO: add/remove tags
  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Optional<Book> updateBookTags(long id, Set<String> newTags) {
    return Optional.empty();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Optional<Book> addNewTag(Long bookId, Long tagId) {
    var targetTagOpt = jpaTagsRepository.findById(tagId);

    return targetTagOpt.flatMap(tag -> updateBook(bookId, book -> book.assignTag(tag)));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Optional<Book> removeTag(Long bookId, Long tagId) {
    var targetTagOpt = jpaTagsRepository.findById(tagId);

    return targetTagOpt.flatMap(tag -> updateBook(bookId, book -> book.deassignTag(tag)));
  }

  private Optional<Book> updateBook(long id, Consumer<Book> update) {
    var target = findBook(id);
    if (target.isEmpty()) return target;

    var targetBook = target.get();

    update.accept(targetBook);

    return target;
  }
}
