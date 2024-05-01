package denvot.homework.bookService.controllers.requests;

import denvot.homework.bookService.data.entities.Role;

import java.util.Set;

public record UserRegisterRequest(String username, String password, Set<Role> roles) {
}
