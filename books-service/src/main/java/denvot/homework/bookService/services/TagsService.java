package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Tag;
import denvot.homework.bookService.data.repositories.jpa.JpaTagsRepository;
import denvot.homework.bookService.exceptions.TagAlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

  @Transactional(propagation = Propagation.REQUIRED)
  public Optional<Tag> rename(long id, String newName) throws TagAlreadyExistsException {
    var tagWithSameNameOpt = jpaTagsRepository.findByName(newName);

    if (tagWithSameNameOpt.isPresent() && tagWithSameNameOpt.get().getId() != id) {
      throw new TagAlreadyExistsException();
    }

    var target = jpaTagsRepository.findById(id);

    if (target.isEmpty()) return Optional.empty();

    var targetTag = target.get();

    targetTag.setName(newName);
    return target;
  }
}
