package denvot.homework.bookService.data.repositories;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface BooksRepositoryBase {
  Book createBook(String author, String title, Set<String> tags);

  Book findBook(long id) throws BookNotFoundException;

  void deleteBook(long id) throws BookNotFoundException;

  ArrayList<Book> getByTags(Set<String> objects);

  List<Book> getAllBooks();
}
