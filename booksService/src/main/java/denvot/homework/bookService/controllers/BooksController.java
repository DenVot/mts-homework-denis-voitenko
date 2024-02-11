package denvot.homework.bookService.controllers;

import denvot.homework.bookService.controllers.requests.BookCreationRequest;
import denvot.homework.bookService.controllers.requests.BookUpdateRequest;
import denvot.homework.bookService.controllers.responses.BookApiEntity;
import denvot.homework.bookService.data.entities.BookId;
import denvot.homework.bookService.exceptions.InvalidBookDataException;
import denvot.homework.bookService.services.BookCreationInfo;
import denvot.homework.bookService.services.BookUpdateBuilder;
import denvot.homework.bookService.services.BooksServiceBase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    var bookResult = booksService.createNew(
            new BookCreationInfo(bookCreationRequest.getAuthor(),
                    bookCreationRequest.getTitle(),
                    Set.of(bookCreationRequest.getTags())));

    return BookApiEntity.fromBook(bookResult);
  }

  @GetMapping("/tags/{tag}")
  public List<BookApiEntity> getBooksByTag(@PathVariable("tag") String tag) {
    var books = booksService.getBooksByTags(Set.of(tag));

    return books.stream().map(BookApiEntity::fromBook).collect(Collectors.toList());
  }

  @GetMapping("{id}")
  public ResponseEntity<BookApiEntity> getBook(@PathVariable("id") int id) {
    var book = booksService.findBook(new BookId(id));

    return book.map(value -> new ResponseEntity<>(BookApiEntity.fromBook(value), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @PatchMapping("{id}")
  public ResponseEntity<BookApiEntity> updateBook(
          @PathVariable("id") int id,
          @RequestBody BookUpdateRequest updateRequest) {
    var updateStrategyBuilder = new BookUpdateBuilder();

    if(updateRequest.newAuthor() != null) {
      updateStrategyBuilder.withAuthor(updateRequest.newAuthor());
    }

    if (updateRequest.newTitle() != null) {
      updateStrategyBuilder.withTitle(updateRequest.newTitle());
    }

    if (updateRequest.newTags() != null) {
      updateStrategyBuilder.withNewTags(Set.of(updateRequest.newTags()));
    }

    var updateStrategy = updateStrategyBuilder.build();
    var book = booksService.updateBook(new BookId(id), updateStrategy);

    return book.map(value -> new ResponseEntity<>(BookApiEntity.fromBook(value), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }
}
