package denvot.homework.bookpurchaseservice.services;

import denvot.homework.bookpurchaseservice.data.entities.Wallet;
import denvot.homework.bookpurchaseservice.data.repositories.WalletRepository;
import denvot.homework.bookpurchaseservice.exceptions.NotEnoughMoneyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService implements WalletServiceBase {
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

  @Transactional
  public void pay(int amount) {
    var wallet = getWallet();
    var walletBalance = wallet.getBalance();

    if (walletBalance - amount >= 0) {
      wallet.setBalance(walletBalance - amount);
    } else {
      throw new NotEnoughMoneyException();
    }

    repository.save(wallet);
  }
}
