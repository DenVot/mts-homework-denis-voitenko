package denvot.homework.bookService.data.repositories;

import denvot.homework.bookService.data.entities.Book;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

public class HashMapBooksRepositoryGetBooksByTagsTests {
  private HashMap<Long, Book> books;
  private HashMapBooksRepository repository;

  @BeforeEach
  public void setUp() {
    books = new HashMap<>();
    repository = new HashMapBooksRepository(books);
  }

  @Test
  public void testGetByTagsEmptyBecauseQueryIsEmpty() {
    var tags = new HashSet<String>();
    tags.add("Романы");

    books.put(1L, new Book(1, null, null, tags));

    Assertions.assertEquals(0, repository.getByTags(new HashSet<>()).size());
  }

  @Test
  public void testGetByTagsEmptyBecauseNoTags() {
    var tags = new HashSet<String>();
    tags.add("Романы");

    books.put(1L, new Book(1, null, null, new HashSet<>()));

    Assertions.assertEquals(0, repository.getByTags(tags).size());
  }

  @Test
  public void testGetByTagsNotEmpty() {
    var originalTags = new HashSet<String>();
    originalTags.add("Романы");
    originalTags.add("Триллеры");

    var queryTags = new HashSet<String>();
    queryTags.add("Романы");

    var booksTestData = new Book[] {
            new Book(1, "Кент Бек", "TDD", new HashSet<>()),
            new Book(2, "Пупу", "Роман", queryTags),
            new Book(3, "Пупу", "Роман-Триллер", originalTags),
    };

    for (Book book : booksTestData) {
      books.put(book.getId(), book);
    }

    Assertions.assertEquals(2, repository.getByTags(queryTags).size());
  }
}
