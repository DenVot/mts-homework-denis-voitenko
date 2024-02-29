package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Author;

import java.util.Optional;

public interface AuthorsServiceBase {
  Author createNew(String firstName, String lastName);

  Optional<Author> findAuthor(long id);

  Optional<Author> updateAuthor(Long id, AuthorUpdateDto update);

  boolean deleteAuthor(Long id);
}
