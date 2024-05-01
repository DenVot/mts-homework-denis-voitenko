package denvot.homework.bookService.controllers;

import denvot.homework.bookService.controllers.requests.UserRegisterRequest;
import denvot.homework.bookService.data.entities.User;
import denvot.homework.bookService.data.repositories.jpa.JpaUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
  private final JpaUserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserController(JpaUserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping
  @Transactional
  public void register(@RequestBody UserRegisterRequest request) {
    userRepository.save(
      new User(
        request.username(),
        passwordEncoder.encode(request.password()),
        request.roles()
      )
    );
  }
}
