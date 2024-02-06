package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.repositories.HashMapBooksRepository;
import denvot.homework.bookService.exceptions.InvalidBookDataException;

import java.util.Set;

public class BookService {

  private final HashMapBooksRepository booksRepository;

  public BookService(HashMapBooksRepository booksRepository) {
    this.booksRepository = booksRepository;
  }

  public Book createNew(String author, String title, Set<String> tags) throws InvalidBookDataException {
    if (author == null || title == null || tags == null) {
      throw new InvalidBookDataException();
    }

    return booksRepository.createBook(author, title, tags);
  }
}
