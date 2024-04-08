package denvot.homework.bookpurchaseservice.data.repositories;

import denvot.homework.bookpurchaseservice.data.entities.OutboxRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<OutboxRecord, Long> {}
