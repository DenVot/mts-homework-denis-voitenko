package denvot.homework.authorsregistry.controllers;

import denvot.homework.authorsregistry.Tuple;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
public class AuthorsRegistryController {
  private final Set<Tuple<String, String>> authorsBooks =
          Set.of(new Tuple<>("Кент Бек", "TDD"));

  private final HashSet<String> requests = new HashSet<>();

  @GetMapping("/api/authors-registry/is-wrote-this-book")
  public String isWrote(@RequestParam("firstName") String firstName,
                         @RequestParam("lastName") String lastName,
                         @RequestParam("bookName") String bookName,
                         @RequestHeader("X-REQUEST-ID") Optional<String> uuid) {
    if (uuid.isPresent()) {
      if (requests.contains(uuid.get())) {
        requests.add(uuid.get());
        return "true";
      }

      requests.add(uuid.get());
    }

    return Boolean.toString(authorsBooks.contains(new Tuple<>(firstName + " " + lastName, bookName)));
  }
}
