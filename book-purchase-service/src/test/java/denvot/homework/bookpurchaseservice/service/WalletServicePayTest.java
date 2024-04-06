package denvot.homework.bookpurchaseservice.service;

import denvot.homework.bookpurchaseservice.DatabaseSuite;
import denvot.homework.bookpurchaseservice.data.entities.Wallet;
import denvot.homework.bookpurchaseservice.data.repositories.WalletRepository;
import denvot.homework.bookpurchaseservice.exceptions.NotEnoughMoneyException;
import denvot.homework.bookpurchaseservice.services.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        "wallet-balance = 100"
})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import({WalletService.class})
public class WalletServicePayTest extends DatabaseSuite {
  @Autowired
  private WalletService walletService;

  @Autowired
  private WalletRepository walletRepository;

  private Wallet testWallet;

  @BeforeEach
  public void setUp() {
    walletRepository.deleteAll();
    testWallet = new Wallet(100);

    walletRepository.save(testWallet);
  }

  @Test
  public void testPay() {
    assertDoesNotThrow(() -> walletService.pay(100));
    assertEquals(0, walletRepository.findAll().get(0).getBalance());
  }

  @Test
  public void testPayNotEnoughBalance() {
    assertDoesNotThrow(() -> walletService.pay(100));
    assertThrows(NotEnoughMoneyException.class, () -> walletService.pay(100));
  }
}
