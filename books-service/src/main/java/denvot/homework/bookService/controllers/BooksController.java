package denvot.homework.bookService.controllers;

import denvot.homework.bookService.controllers.requests.BookCreationRequest;
import denvot.homework.bookService.controllers.requests.BookUpdateRequest;
import denvot.homework.bookService.controllers.responses.BookApiEntity;
import denvot.homework.bookService.data.entities.Book;
import denvot.homework.bookService.exceptions.InvalidBookDataException;
import denvot.homework.bookService.services.BookCreationInfo;
import denvot.homework.bookService.services.BooksServiceBase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class BooksController {
  private final BooksServiceBase booksService;

  public BooksController(@Autowired BooksServiceBase booksService) {
    this.booksService = booksService;
  }

  @PostMapping
  public BookApiEntity createBook(
          @Valid @RequestBody BookCreationRequest bookCreationRequest) throws InvalidBookDataException {
    var bookResult = booksService.createNew(
            new BookCreationInfo(bookCreationRequest.getAuthorId(), bookCreationRequest.getTitle()));

    return BookApiEntity.fromBook(bookResult);
  }

  @DeleteMapping("{id}")
  public void deleteBook(@PathVariable("id") long id) {
    booksService.deleteBook(id);
  }

  @GetMapping("tags/{tag}")
  public List<BookApiEntity> getBooksByTag(@PathVariable("tag") long tagId) {
    var books = booksService.getBooksByTag(tagId);

    return books.stream().map(BookApiEntity::fromBook).collect(Collectors.toList());
  }

  @GetMapping("{id}")
  public ResponseEntity<BookApiEntity> getBook(@PathVariable("id") long id) {
    var book = booksService.findBook(id);

    return book.map(value -> new ResponseEntity<>(BookApiEntity.fromBook(value), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @PatchMapping("{id}")
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
