package denvot.homework.bookService.data.repositories;

import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.entities.BookId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

class HashMapBooksRepositoryCreateBookTests {
  private HashMapBooksRepository hashMapBooksRepository;
  private HashMap<BookId, Book> hashMap;

  @BeforeEach
  void setUp() {
    hashMap = new HashMap<>();
    hashMapBooksRepository = new HashMapBooksRepository(hashMap);
  }

  @Test
  void testSimpleCreationOfBook() {
    hashMapBooksRepository.createBook("Кент Бек", "TDD", new HashSet<>());
    Assertions.assertFalse(hashMap.isEmpty());
  }
}