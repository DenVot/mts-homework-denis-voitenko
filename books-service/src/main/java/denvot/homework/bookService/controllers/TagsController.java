package denvot.homework.bookService.controllers;

import denvot.homework.bookService.controllers.requests.TagCreationRequest;
import denvot.homework.bookService.controllers.responses.TagApiEntity;
import denvot.homework.bookService.exceptions.TagAlreadyExistsException;
import denvot.homework.bookService.services.TagsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/tags")
public class TagsController {
  private final TagsService tagsService;

  public TagsController(TagsService tagsService) {
    this.tagsService = tagsService;
  }

  @GetMapping("{id}")
  public ResponseEntity<TagApiEntity> findTag(@PathVariable("id") long id) {
    var target = tagsService.findTag(id);

    return target.map(tag -> ResponseEntity.ok(TagApiEntity.fromTag(tag)))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @PostMapping
  public ResponseEntity<TagApiEntity> createTag(@RequestBody TagCreationRequest creationData) throws TagAlreadyExistsException {
    return ResponseEntity.ok(TagApiEntity.fromTag(tagsService.createNew(creationData.name())));
  }

  @ExceptionHandler
  public ResponseEntity<TagApiEntity> noSuchElementExceptionHandler(TagAlreadyExistsException ex) {
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }
}
