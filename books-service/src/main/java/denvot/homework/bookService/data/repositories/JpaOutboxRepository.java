package denvot.homework.bookService.data.repositories;

import denvot.homework.bookService.data.entities.OutboxRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOutboxRepository extends JpaRepository<OutboxRecord, Long> {}
