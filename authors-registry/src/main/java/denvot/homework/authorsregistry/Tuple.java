package denvot.homework.authorsregistry;

import java.util.Objects;

public record Tuple<T1, T2>(T1 item1, T2 item2) {
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Tuple<?, ?> tuple = (Tuple<?, ?>) o;
    return Objects.equals(item1, tuple.item1) && Objects.equals(item2, tuple.item2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(item1, item2);
  }
}
