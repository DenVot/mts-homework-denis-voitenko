package denvot.homework.bookService.services;

import java.util.Set;

public record BookCreationInfo(String author, String title, Set<String> tags) {
}
