package denvot.homework.bookpurchaseservice.services;

import denvot.homework.bookpurchaseservice.exceptions.NotEnoughMoneyException;
import org.springframework.beans.factory.annotation.Value;

public class WalletService implements WalletServiceBase {
  private int amount;

  public WalletService(
          @Value("${wallet-balance}") int amount) {
    this.amount = amount;
  }

  @Override
  public void pay(int cost) {
    if (amount - cost < 0) {
      throw new NotEnoughMoneyException();
    }

    amount -= cost;
  }
}
