package denvot.homework.bookpurchaseservice.services;

import denvot.homework.bookpurchaseservice.data.entities.Wallet;
import denvot.homework.bookpurchaseservice.data.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
  private final WalletRepository repository;
  private final int defaultBalance;

  public WalletService(WalletRepository repository,
                       @Value("${wallet-balance}") int defaultBalance) {
    this.repository = repository;
    this.defaultBalance = defaultBalance;
  }

  public Wallet getWallet() {
    if (repository.count() == 0) {
      var wallet = new Wallet(defaultBalance);

      repository.save(wallet);
    }

    return repository.findAll().get(0);
  }
}
