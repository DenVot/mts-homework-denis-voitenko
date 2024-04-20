package denvot.homework.bookService;

import java.util.List;

public class ArrayHelper {
  @SuppressWarnings("unchecked")
  public static <T> T[] toArray(List<T> list) {
    var arr = (T[]) new Object[list.size()];

    for (int i = 0; i < list.size(); i++) {
      arr[i] = list.get(i);
    }

    return arr;
  }
}
