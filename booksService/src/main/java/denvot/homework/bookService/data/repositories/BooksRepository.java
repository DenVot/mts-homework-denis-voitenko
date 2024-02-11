package denvot.homework.bookService.data.repositories;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.entities.BookId;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface BooksRepository {
  Book createBook(String author, String title, Set<String> tags);

  Book findBook(BookId id) throws BookNotFoundException;

  void deleteBook(BookId id) throws BookNotFoundException;

  ArrayList<Book> getByTags(Set<String> objects);

  List<Book> getAllBooks();
}
