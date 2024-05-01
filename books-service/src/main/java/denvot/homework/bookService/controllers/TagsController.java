package denvot.homework.bookService.controllers;

import denvot.homework.bookService.controllers.requests.TagCreationRequest;
import denvot.homework.bookService.controllers.requests.TagUpdateRequest;
import denvot.homework.bookService.controllers.responses.TagApiEntity;
import denvot.homework.bookService.exceptions.TagAlreadyExistsException;
import denvot.homework.bookService.services.TagsServiceBase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/tags")
@PreAuthorize("isAuthenticated()")
public class TagsController {
  private final TagsServiceBase tagsService;

  public TagsController(TagsServiceBase tagsService) {
    this.tagsService = tagsService;
  }

  @GetMapping("{id}")
  public ResponseEntity<TagApiEntity> findTag(@PathVariable("id") long id) {
    var target = tagsService.findTag(id);

    return target.map(tag -> ResponseEntity.ok(TagApiEntity.fromTag(tag)))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @DeleteMapping("{id}")
  public void deleteTag(@PathVariable("id") long id) {
    tagsService.deleteTag(id);
  }

  @PostMapping
  public ResponseEntity<TagApiEntity> createTag(@RequestBody TagCreationRequest creationData) throws TagAlreadyExistsException {
    return ResponseEntity.ok(TagApiEntity.fromTag(tagsService.createNew(creationData.name())));
  }

  @PatchMapping("{id}")
  public ResponseEntity<TagApiEntity> renameTag(@PathVariable long id, @RequestBody TagUpdateRequest updateData) throws TagAlreadyExistsException {
    var target = tagsService.rename(id, updateData.newName());

    return target.map(tag -> ResponseEntity.ok(TagApiEntity.fromTag(tag)))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @ExceptionHandler
  public ResponseEntity<TagApiEntity> noSuchElementExceptionHandler(TagAlreadyExistsException ex) {
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }
}
