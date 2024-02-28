package denvot.homework.bookService.data.repositories;

import denvot.homework.bookService.data.entities.Author;
import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;

import java.util.List;
import java.util.Set;

public interface BooksRepositoryBase {
  Book createBook(Author author, String title);

  Book findBook(long id) throws BookNotFoundException;

  void deleteBook(long id) throws BookNotFoundException;

  List<Book> getByTags(Set<String> objects);

  List<Book> getAllBooks();
}
