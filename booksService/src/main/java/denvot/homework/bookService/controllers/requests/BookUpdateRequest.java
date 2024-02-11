package denvot.homework.bookService.controllers.requests;

public record BookUpdateRequest(String newAuthor, String newTitle, String[] newTags) {
}
