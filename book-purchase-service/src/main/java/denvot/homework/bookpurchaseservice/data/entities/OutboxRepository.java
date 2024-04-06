package denvot.homework.bookpurchaseservice.data.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<OutboxRecord, Long> {}
