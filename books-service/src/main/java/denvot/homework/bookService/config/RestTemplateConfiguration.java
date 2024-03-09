package denvot.homework.bookService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {
  @Bean
  public RestTemplate restTemplate(
          @Value("${authors-registry.service.base.url}") String baseUrl) {
    return new RestTemplateBuilder()
            .rootUri(baseUrl)
            .build();
  }
}
