package denvot.homework.bookratingservice.helpers;

import org.springframework.stereotype.Service;

@Service
public class RandomBookRatingProvider implements BookRatingProviderBase {
  @Override
  public int rate(long bookId) {
    return 0;
  }
}
