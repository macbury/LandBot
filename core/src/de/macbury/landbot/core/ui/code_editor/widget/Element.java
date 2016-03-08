package de.macbury.landbot.core.ui.code_editor.widget;

import de.macbury.landbot.core.ui.code_editor.js.LuaScanner;

public class Element {
  public LuaScanner.Kind kind;
  public String text;

  public Element(LuaScanner.Kind k, String t) {
    text = t;
    kind = k;
  }

  public int getSize() {
    return text.length();
  }

  public int countSpaces() {
    int sum = 0;

    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);
      if (c == ' ') {
        sum += 1;
      } else {
        break;
      }
    }

    return sum;
  }
}