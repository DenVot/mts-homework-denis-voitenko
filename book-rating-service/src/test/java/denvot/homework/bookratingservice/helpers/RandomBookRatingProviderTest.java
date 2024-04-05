package denvot.homework.bookratingservice.helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomBookRatingProviderTest {
  @Test
  public void testRandomRating() {
    var provider = new RandomBookRatingProvider();
    var rate = provider.rate(0);

    assertTrue(rate >= 0 && rate <= 10);
  }
}
