package denvot.homework.bookpurchaseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BookPurchaseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookPurchaseServiceApplication.class, args);
	}

}
