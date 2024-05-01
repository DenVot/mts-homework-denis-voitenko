package denvot.homework.bookService;

import java.util.List;

public class ArrayHelper {
  @SuppressWarnings("unchecked")
  public static String[] toArray(List<String> list) {
    var arr = new String[list.size()];

    for (int i = 0; i < list.size(); i++) {
      arr[i] = list.get(i);
    }

    return arr;
  }
}
