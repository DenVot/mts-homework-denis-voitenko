package denvot.homework.bookService.data.repositories.jpa;

import denvot.homework.bookService.data.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTagsRepository extends JpaRepository<Tag, Long> {
}