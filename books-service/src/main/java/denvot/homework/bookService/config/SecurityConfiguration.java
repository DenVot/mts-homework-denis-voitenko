package denvot.homework.bookService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.NEVER;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  return http.authorizeHttpRequests(expressionInterceptUrlRegistry ->
                  expressionInterceptUrlRegistry.anyRequest().authenticated())
          .httpBasic(Customizer.withDefaults())
          .csrf().disable()
          .cors().disable()
          .build();
}
}
