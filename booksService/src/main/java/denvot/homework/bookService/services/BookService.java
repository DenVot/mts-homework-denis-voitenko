package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.repositories.HashMapBooksRepository;
import denvot.homework.bookService.exceptions.InvalidBookDataException;

public class BookService {

  private final HashMapBooksRepository booksRepository;

  public BookService(HashMapBooksRepository booksRepository) {
    this.booksRepository = booksRepository;
  }

  public Book createNew(BookCreationInfo creationInfo) throws InvalidBookDataException {
    if (creationInfo.author() == null ||
            creationInfo.title() == null ||
            creationInfo.tags() == null) {
      throw new InvalidBookDataException();
    }

    return booksRepository.createBook(creationInfo.author(),
            creationInfo.title(),
            creationInfo.tags());
  }
}
