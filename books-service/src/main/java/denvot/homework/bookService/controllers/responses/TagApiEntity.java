package denvot.homework.bookService.controllers.responses;

import denvot.homework.bookService.data.entities.Tag;

public record TagApiEntity(long id, String name) {
  public static TagApiEntity fromTag(Tag tag) {
    return new TagApiEntity(tag.getId(), tag.getName());
  }
}
