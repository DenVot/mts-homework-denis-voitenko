package denvot.homework.bookService.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import denvot.homework.bookService.controllers.requests.BookCreationRequest;
import denvot.homework.bookService.controllers.requests.BookUpdateRequest;
import denvot.homework.bookService.controllers.responses.BookApiEntity;
import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.data.repositories.exceptions.BookNotFoundException;
import denvot.homework.bookService.exceptions.InvalidBookDataException;
import denvot.homework.bookService.services.BookCreationInfo;
import denvot.homework.bookService.services.BooksPurchasingManagerBase;
import denvot.homework.bookService.services.BooksServiceBase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/api/books")
@PreAuthorize("isAuthenticated()")
public class BooksController {
  private final BooksServiceBase booksService;
  private final BooksPurchasingManagerBase booksPurchasingManager;

  public BooksController(BooksServiceBase booksService,
                         BooksPurchasingManagerBase booksPurchasingManager) {
    this.booksService = booksService;
    this.booksPurchasingManager = booksPurchasingManager;
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public BookApiEntity createBook(
          @Valid @RequestBody BookCreationRequest bookCreationRequest) throws InvalidBookDataException {
    var bookResult = booksService.createNew(
            new BookCreationInfo(bookCreationRequest.authorId(), bookCreationRequest.title()));

    return BookApiEntity.fromBook(bookResult);
  }

  @DeleteMapping("{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public void deleteBook(@PathVariable("id") long id) {
    booksService.deleteBook(id);
  }

  @GetMapping("tags/{tag}")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT')")
  public List<BookApiEntity> getBooksByTag(@PathVariable("tag") long tagId) {
    var books = booksService.getBooksByTag(tagId);

    return books.stream().map(BookApiEntity::fromBook).collect(Collectors.toList());
  }

  @GetMapping("{id}")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT')")
  public ResponseEntity<BookApiEntity> getBook(@PathVariable("id") long id) {
    var book = booksService.findBook(id);

    return book.map(value -> new ResponseEntity<>(BookApiEntity.fromBook(value), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @PatchMapping("{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<BookApiEntity> updateBook(
          @PathVariable("id") long id,
          @RequestBody BookUpdateRequest updateRequest) {
    Optional<Book> book = Optional.empty();

    if(updateRequest.newAuthorId() != null) {
      book = booksService.updateBookAuthor(id, updateRequest.newAuthorId());
    }

    if (updateRequest.newTitle() != null) {
      book = booksService.updateBookTitle(id, updateRequest.newTitle());
    }

    return book.map(value -> new ResponseEntity<>(BookApiEntity.fromBook(value), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @PatchMapping("{book_id}/tags/{tag_id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<BookApiEntity> addTagToBook(
          @PathVariable("book_id") long bookId,
          @PathVariable("tag_id") long tagId) {
    var targetBook = booksService.addNewTag(bookId, tagId);

    if (targetBook.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return targetBook.map(value -> new ResponseEntity<>(BookApiEntity.fromBook(value), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @DeleteMapping("{book_id}/tags/{tag_id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<BookApiEntity> removeTagFromBook(
          @PathVariable("book_id") long bookId,
          @PathVariable("tag_id") long tagId) {
    var targetBook = booksService.removeTag(bookId, tagId);

    if (targetBook.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return targetBook.map(value -> new ResponseEntity<>(BookApiEntity.fromBook(value), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @GetMapping("/books")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT')")
  public String getBooksView(Model model) {
    var apiBooks = booksService.getAllBooks()
            .stream()
            .map(BookApiEntity::fromBook)
            .sorted(Comparator.comparing(BookApiEntity::id))
            .collect(Collectors.toList());

    model.addAttribute("books", apiBooks);

    return "books";
  }

  @PostMapping("/books/{book_id}/purchase")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT')")
  public void createPurchase(@PathVariable("book_id") Long bookId) throws BookNotFoundException, JsonProcessingException {
    booksPurchasingManager.createPurchasing(bookId);
  }
}
