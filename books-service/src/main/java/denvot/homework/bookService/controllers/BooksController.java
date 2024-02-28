package denvot.homework.bookService.controllers;

import denvot.homework.bookService.controllers.requests.BookCreationRequest;
import denvot.homework.bookService.controllers.requests.BookUpdateRequest;
import denvot.homework.bookService.controllers.responses.BookApiEntity;
import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.exceptions.InvalidBookDataException;
import denvot.homework.bookService.services.BookCreationInfo;
import denvot.homework.bookService.services.BooksServiceBase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/api/books")
public class BooksController {
  private final BooksServiceBase booksService;

  public BooksController(@Autowired BooksServiceBase booksService) {
    this.booksService = booksService;
  }

  @PostMapping
  public BookApiEntity createBook(
          @Valid @RequestBody BookCreationRequest bookCreationRequest) throws InvalidBookDataException {
    /*var bookResult = booksService.createNew(
            new BookCreationInfo(bookCreationRequest.getAuthor(),
                    bookCreationRequest.getTitle(),
                    Set.of(bookCreationRequest.getTags())));*/

    return null;
  }

  @GetMapping("/tags/{tag}")
  public List<BookApiEntity> getBooksByTag(@PathVariable("tag") String tag) {
    var books = booksService.getBooksByTags(Set.of(tag));

    return books.stream().map(BookApiEntity::fromBook).collect(Collectors.toList());
  }

  @GetMapping("{id}")
  public ResponseEntity<BookApiEntity> getBook(@PathVariable("id") int id) {
    var book = booksService.findBook(id);

    return book.map(value -> new ResponseEntity<>(BookApiEntity.fromBook(value), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @PatchMapping("{id}")
  public ResponseEntity<BookApiEntity> updateBook(
          @PathVariable("id") int id,
          @RequestBody BookUpdateRequest updateRequest) {
    Optional<Book> book = Optional.empty();

    if(updateRequest.newAuthor() != null) {
      book = booksService.updateBookAuthor(id, updateRequest.newAuthor());
    }

    if (updateRequest.newTitle() != null) {
      book = booksService.updateBookTitle(id, updateRequest.newTitle());
    }

    if (updateRequest.newTags() != null) {
      book = booksService.updateBookTags(id, Set.of(updateRequest.newTags()));
    }

    return book.map(value -> new ResponseEntity<>(BookApiEntity.fromBook(value), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @GetMapping("/books")
  public String getBooksView(Model model) {
    var apiBooks = booksService.getAllBooks()
            .stream()
            .map(BookApiEntity::fromBook)
            .sorted(Comparator.comparing(BookApiEntity::id))
            .collect(Collectors.toList());

    model.addAttribute("books", apiBooks);

    return "books";
  }
}
