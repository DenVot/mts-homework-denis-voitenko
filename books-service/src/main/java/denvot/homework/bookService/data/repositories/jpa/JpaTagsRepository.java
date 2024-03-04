package denvot.homework.bookService.data.repositories.jpa;

import denvot.homework.bookService.data.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaTagsRepository extends JpaRepository<Tag, Long> {
  Optional<Tag> findByName(String name);
}