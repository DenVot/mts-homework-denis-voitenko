package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Tag;
import denvot.homework.bookService.exceptions.TagAlreadyExistsException;

import java.util.Optional;

public interface TagsServiceBase {
  Tag createNew(String tagName) throws TagAlreadyExistsException;

  boolean deleteTag(long id);

  Optional<Tag> rename(long id, String newName) throws TagAlreadyExistsException;

  Optional<Tag> findTag(Long id);
}
