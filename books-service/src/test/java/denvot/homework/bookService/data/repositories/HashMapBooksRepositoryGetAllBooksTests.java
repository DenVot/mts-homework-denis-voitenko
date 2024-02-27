package denvot.homework.bookService.data.repositories;

import denvot.homework.bookService.data.entities.Book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class HashMapBooksRepositoryGetAllBooksTests {
  private HashMap<Long, Book> booksTable;
  private HashMapBooksRepository repository;

  @BeforeEach
  public void setUp () {
    booksTable = new HashMap<>();
    repository = new HashMapBooksRepository(booksTable);
  }

  @Test
  public void testGetAllBooks() {
    booksTable.putAll(Stream.of(
            new Book(1, "Джеффри Рихтер", "CLR via C#", Set.of("Записки сумасшедшего")),
            new Book(2, "Кент Бек", "TDD", Set.of("Записки гения")),
            new Book(3, "Джоан Роулинг", "Гарри Поттер", Set.of("Чьи-то записки")))
            .collect(Collectors.toMap(Book::getId, Function.identity())));

    List<Book> books = repository.getAllBooks();

    assertEquals(booksTable.keySet().size(), books.size());

    for (Book book : books) {
      assertTrue(booksTable.containsKey(book.getId()));
    }
  }
}
