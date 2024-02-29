package denvot.homework.bookService.controllers.requests;

import jakarta.validation.constraints.NotNull;

public record TagCreationRequest(@NotNull String name) {
}
