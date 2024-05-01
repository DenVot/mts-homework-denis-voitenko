package denvot.homework.bookService.services;

import denvot.homework.bookService.data.entities.Role;
import denvot.homework.bookService.data.repositories.jpa.JpaUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static denvot.homework.bookService.ArrayHelper.toArray;

@Service
public class UserAuthService implements UserDetailsService {
  private final JpaUserRepository userRepository;

  public UserAuthService(JpaUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var res = userRepository.findByUsername(username)
            .map(user ->
                    User.withUsername(user.getUsername())
                            .authorities(toArray(user.getRoles().stream()
                                    .map(Role::name)
                                    .collect(Collectors.toList())))
                            .password(user.getPassword())
                            .build());

    return res.get();
  }
}
