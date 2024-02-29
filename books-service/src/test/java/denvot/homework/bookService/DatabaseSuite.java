package denvot.homework.bookService;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

@ContextConfiguration(initializers = DatabaseSuite.Initializer.class)
public class DatabaseSuite {
  private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:13");

  static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
      Startables.deepStart(POSTGRES).join();

      TestPropertyValues.of(
              "spring.datasource.url=" + POSTGRES.getJdbcUrl(),
              "spring.datasource.username=" + POSTGRES.getUsername(),
              "spring.datasource.password=" + POSTGRES.getPassword()
              //"spring.jpa.hibernate.ddl-auto=create"
      ).applyTo(context);
    }
  }
}
