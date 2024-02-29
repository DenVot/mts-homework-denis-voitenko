package denvot.homework.bookService.data.repositories.jpa;

import denvot.homework.bookService.data.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAuthorsRepository extends JpaRepository<Author, Long> {
}