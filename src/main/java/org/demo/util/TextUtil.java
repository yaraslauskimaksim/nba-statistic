package org.demo.util;

public class TextUtil {

  public static String formatText(String text, Object... args) {
    return String.format(text, args);
  }
}
