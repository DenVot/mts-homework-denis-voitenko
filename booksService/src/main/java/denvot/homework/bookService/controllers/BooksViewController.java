package denvot.homework.bookService.controllers;

import denvot.homework.bookService.controllers.responses.BookApiEntity;
import denvot.homework.bookService.services.BooksServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.Collectors;

@Controller
public class BooksViewController {
  private final BooksServiceBase booksService;

  public BooksViewController(@Autowired BooksServiceBase booksService) {
    this.booksService = booksService;
  }

  @GetMapping("/books")
  public String getBooksView(Model model) {
    var apiBooks = booksService.getAllBooks()
            .stream()
            .map(BookApiEntity::fromBook)
            .collect(Collectors.toList());

    model.addAttribute("books", apiBooks);

    return "books";
  }
}
