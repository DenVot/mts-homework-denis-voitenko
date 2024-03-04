package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Author;
import denvot.homework.bookService.data.repositories.jpa.JpaAuthorsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthorsService implements AuthorsServiceBase {
  private final JpaAuthorsRepository authorsRepository;

  public AuthorsService(JpaAuthorsRepository authorsRepository) {

    this.authorsRepository = authorsRepository;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Author createNew(String firstName, String lastName) {
    var author = new Author(firstName, lastName);

    return authorsRepository.save(author);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Optional<Author> findAuthor(long id) {
    return authorsRepository.findById(id);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Optional<Author> updateAuthor(Long id, AuthorUpdateDto update) {
    var targetAuthorOpt = authorsRepository.findById(id);

    if (targetAuthorOpt.isEmpty()) return targetAuthorOpt;

    var targetAuthor = targetAuthorOpt.get();

    if (update.newFirstName().isPresent()) {
      targetAuthor.setFirstName(update.newFirstName().get());
    }

    if (update.newLastName().isPresent()) {
      targetAuthor.setLastName(update.newLastName().get());
    }

    return targetAuthorOpt;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public boolean deleteAuthor(Long id) {
    var target = authorsRepository.findById(id);

    if (target.isEmpty()) return false;

    authorsRepository.deleteById(id);

    return true;
  }
}
