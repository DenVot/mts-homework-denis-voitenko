package denvot.homework.bookService.data.repositories;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.entities.BookId;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class HashMapBooksRepository implements BooksRepository {
  private static final AtomicInteger nextBookId = new AtomicInteger();
  private final HashMap<BookId, Book> books;

  public HashMapBooksRepository(HashMap<BookId, Book> books) {
    this.books = books;
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
  public ArrayList<Book> getByTags(HashSet<String> tags) {
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

  private void failIfBookNotExists(BookId id) throws BookNotFoundException {
    if (!books.containsKey(id)) {
      throw new BookNotFoundException();
    }
  }
}
