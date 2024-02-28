package denvot.homework.bookService.data.repositories;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.entities.BookId;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class HashMapBooksRepository implements BooksRepositoryBase {
  private static final AtomicInteger nextBookId = new AtomicInteger();
  private final HashMap<BookId, Book> books;

  public HashMapBooksRepository(HashMap<BookId, Book> books) {
    this.books = books;
  }

  public HashMapBooksRepository() {
    this(new HashMap<>());
  }

  @Override
  public Book createBook(String author, String title, Set<String> tags) {
    var bookId = new BookId(nextBookId.getAndIncrement());
    var book = new Book(bookId, author, title, tags);
    books.put(bookId, book);

    return book;
  }

  @Override
  public Book findBook(BookId id) throws BookNotFoundException {
    failIfBookNotExists(id);
    return books.get(id);
  }

  @Override
  public void deleteBook(BookId id) throws BookNotFoundException {
    failIfBookNotExists(id);
    books.remove(id);
  }

  @Override
  public ArrayList<Book> getByTags(Set<String> tags) {
    var list = new ArrayList<Book>();

    for (var book : books.values()) {
      var tempTags = new HashSet<>(tags);
      tempTags.retainAll(book.getTags());

      if (!tempTags.isEmpty()) {
        list.add(book);
      }
    }

    return list;
  }

  @Override
  public List<Book> getAllBooks() {
    return books.values().stream().toList();
  }

  private void failIfBookNotExists(BookId id) throws BookNotFoundException {
    if (!books.containsKey(id)) {
      throw new BookNotFoundException();
    }
  }
}
