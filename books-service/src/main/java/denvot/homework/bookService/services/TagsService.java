package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Tag;
import denvot.homework.bookService.data.repositories.jpa.JpaTagsRepository;
import denvot.homework.bookService.exceptions.TagAlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagsService {

  private final JpaTagsRepository jpaTagsRepository;

  public TagsService(JpaTagsRepository jpaTagsRepository) {
    this.jpaTagsRepository = jpaTagsRepository;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public Tag createNew(String tagName) throws TagAlreadyExistsException {
    if (jpaTagsRepository.findByName(tagName).isPresent()) {
      throw new TagAlreadyExistsException();
    }

    return jpaTagsRepository.save(new Tag(tagName));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public boolean deleteTag(long id) {
    jpaTagsRepository.deleteById(id);

    return !jpaTagsRepository.existsById(id);
  }
}
