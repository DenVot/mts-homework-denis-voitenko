package denvot.homework.bookService.data.repositories;

import denvot.homework.bookService.data.entities.Author;
import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;

import java.util.List;

public interface BooksRepositoryBase {
  Book createBook(Author author, String title);

  Book findBook(long id) throws BookNotFoundException;

  void deleteBook(long id) throws BookNotFoundException;

  List<Book> getByTag(long tagId);

  List<Book> getAllBooks();
}
