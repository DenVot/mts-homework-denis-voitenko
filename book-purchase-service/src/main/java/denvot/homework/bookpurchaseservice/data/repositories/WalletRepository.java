package denvot.homework.bookpurchaseservice.data.repositories;

import denvot.homework.bookpurchaseservice.data.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {}
