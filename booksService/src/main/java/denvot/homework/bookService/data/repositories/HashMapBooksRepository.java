package denvot.homework.bookService.data.repositories;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.entities.BookId;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class HashMapBooksRepository {
  private static final AtomicInteger nextBookId = new AtomicInteger();
  private final HashMap<BookId, Book> books;

  public HashMapBooksRepository(HashMap<BookId, Book> books) {
    this.books = books;
  }

  public Book createBook(String author, String title, Set<String> tags) {
    var bookId = new BookId(nextBookId.getAndIncrement());
    var book = new Book(bookId, author, title, tags);
    books.put(bookId, book);

    return book;
  }
}
