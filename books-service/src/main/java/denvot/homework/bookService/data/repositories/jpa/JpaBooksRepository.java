package denvot.homework.bookService.data.repositories.jpa;

import denvot.homework.bookService.data.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaBooksRepository extends JpaRepository<Book, Long> {
  @Query("""
    SELECT bt.book FROM BookTag bt
    JOIN Book
    WHERE bt.id.tagId = :tagId
  """)
  List<Book> findBooksByTagId(Long tagId);
}
