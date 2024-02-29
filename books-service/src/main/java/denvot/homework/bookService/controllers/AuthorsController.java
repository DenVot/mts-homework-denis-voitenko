package denvot.homework.bookService.controllers;

import denvot.homework.bookService.controllers.requests.AuthorCreationRequest;
import denvot.homework.bookService.controllers.requests.AuthorUpdateRequest;
import denvot.homework.bookService.controllers.responses.AuthorApiEntity;
import denvot.homework.bookService.services.AuthorUpdateDto;
import denvot.homework.bookService.services.AuthorsServiceBase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api/authors")
public class AuthorsController {
  private final AuthorsServiceBase authorsService;

  public AuthorsController(AuthorsServiceBase authorsService) {
    this.authorsService = authorsService;
  }

  @PostMapping
  public ResponseEntity<AuthorApiEntity> createAuthor(@RequestBody AuthorCreationRequest creationInfo) {
    return ResponseEntity.ok(
            AuthorApiEntity.fromAuthor(authorsService.createNew(creationInfo.firstName(), creationInfo.lastName())));
  }

  @DeleteMapping("{id}")
  public void deleteAuthor(@PathVariable long id) {
    authorsService.deleteAuthor(id);
  }

  @GetMapping("{id}")
  public ResponseEntity<AuthorApiEntity> findAuthor(@PathVariable("id") long id) {
    var target = authorsService.findAuthor(id);

    return target.map(author -> ResponseEntity.ok(AuthorApiEntity.fromAuthor(author)))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @PatchMapping("{id}")
  public ResponseEntity<AuthorApiEntity> updateAuthor(@PathVariable("id") long id,
                                                      @RequestBody AuthorUpdateRequest updateRequest) {
    var updateDto = new AuthorUpdateDto(
            Optional.ofNullable(updateRequest.newFirstName()),
            Optional.ofNullable(updateRequest.newLastName()));

    var result = authorsService.updateAuthor(id, updateDto);

    return result.map(author -> ResponseEntity.ok(AuthorApiEntity.fromAuthor(author)))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }
}
