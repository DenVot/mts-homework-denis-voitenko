package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Role;
import denvot.homework.bookService.data.repositories.jpa.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import static denvot.homework.bookService.ArrayHelper.toArray;

@Service
public class UserAuthService implements UserDetailsService {
  private final UserRepository userRepository;

  public UserAuthService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
            .map(user ->
                    User.withUsername(username)
                            .authorities(toArray(user.getRoles().stream()
                                    .map(Role::name)
                                    .collect(Collectors.toList())))
                            .password(user.getPassword())
                            .build())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
