package denvot.homework.bookService.controllers.requests;

public record BookUpdateRequest(Long newAuthorId, String newTitle) {
}
