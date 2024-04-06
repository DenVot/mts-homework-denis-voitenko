package denvot.homework.bookpurchaseservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import denvot.homework.bookpurchaseservice.DatabaseSuite;
import denvot.homework.bookpurchaseservice.data.entities.Wallet;
import denvot.homework.bookpurchaseservice.data.repositories.WalletRepository;
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

@SpringBootTest(properties = {
        "wallet-balance = 100"
})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import({WalletService.class})
public class WalletServiceWalletSetupTest extends DatabaseSuite {
  @Autowired
  private WalletRepository walletRepository;

  @Autowired
  private WalletService walletService;

  @BeforeEach
  public void deleteAllWallets() {
    walletRepository.deleteAll();
  }

  @Test
  public void testCreateWallet() {
    Wallet wallet = walletService.getWallet();

    assertNotNull(wallet);
    assertEquals(100, wallet.getBalance());
  }

  @Test
  public void testGetAlreadyCreatedWallet() {
    walletRepository.save(new Wallet(123));

    Wallet wallet = walletService.getWallet();

    assertEquals(123, wallet.getBalance());
  }
}
