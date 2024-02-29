package denvot.homework.bookService.controllers;

import denvot.homework.bookService.controllers.requests.AuthorCreationRequest;
import denvot.homework.bookService.controllers.responses.AuthorApiEntity;
import denvot.homework.bookService.services.AuthorsServiceBase;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
