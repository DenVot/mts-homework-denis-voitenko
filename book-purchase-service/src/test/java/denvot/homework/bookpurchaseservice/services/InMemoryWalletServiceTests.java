package denvot.homework.bookpurchaseservice.services;

import static org.junit.jupiter.api.Assertions.*;

import denvot.homework.bookpurchaseservice.exceptions.NotEnoughMoneyException;
import org.junit.jupiter.api.Test;

public class InMemoryWalletServiceTests {
  @Test
  public void testTransaction() {
    var walletService = new WalletService(100);
    assertDoesNotThrow(() -> walletService.pay(50));
    assertDoesNotThrow(() -> walletService.pay(50));
    assertThrows(NotEnoughMoneyException.class, () -> walletService.pay(50));
  }
}
