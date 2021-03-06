package de.macbury.landbot.core.ui.code_editor.js;

/**
 * A simple Java source file parser for syntax highlighting.
 *
 * @author Matthias Mann
 */
public class LuaScanner {

  private CharacterIterator iterator;

  public enum Kind {
    /** End of file - this token has no text */
    EOF,
    /** End of line - this token has no text */
    NEWLINE,
    /** Normal text - does not include line breaks */
    NORMAL,
    /** A keyword */
    KEYWORD,
    /** A string or character constant */
    STRING,
    /** A comment - multi line comments are split up and {@link NEWLINE} tokens are inserted */
    COMMENT,
    /** A javadoc tag inside a comment */
    COMMENT_TAG,
    NUMBER,
    SPECIAL_KEYWORD
  }

  private static final KeywordList KEYWORD_LIST = new KeywordList(
          "function", "end", "true", "false", "var", "for", "while", "if", "else", "null", "this", "new", "switch", "case", "break", "try", "catch", "do", "instanceof", "return", "throw", "typeof", "with", "prototype");
  private static final KeywordList SPECIAL_KEYWORD_LIST = new KeywordList("robot", "led", "sonar", "console");

  private boolean inMultiLineComment;

  public void set(CharSequence cs) {
    this.iterator = new CharacterIterator(cs);
    inMultiLineComment = false;
  }

  /**
   * Scans for the next token.
   * Read errors result in EOF.
   *
   * Use {@link #getString()} to retrieve the string for the parsed token.
   *
   * @return the next token.
   */
  public Kind scan() {
    iterator.clear();
    if(inMultiLineComment) {
      return scanMultiLineComment(false);
    }
    int ch = iterator.next();
    switch(ch) {
      case CharacterIterator.EOF:
        return Kind.EOF;
      case '\n':
        return Kind.NEWLINE;
      case '\"':
      case '\'':
        scanString(ch);
        return Kind.STRING;
      case '/':
        switch(iterator.peek()) {
          case '/':
            iterator.advanceToEOL();
            return Kind.COMMENT;
          case '*':
            inMultiLineComment = true;
            iterator.next(); // skip '*'
            return scanMultiLineComment(true);
        }
        // fall through
      default:
        return scanNormal(ch);
    }
  }

  /**
   * Returns the string for the last token returned by {@link #scan() }
   *
   * @return the string for the last token
   */
  public String getString() {
    return iterator.getString();
  }

  public int getCurrentPosition() {
    return iterator.getCurrentPosition();
  }

  private void scanString(int endMarker) {
    for(;;) {

      int ch = iterator.next();
      if(ch == '\\') {
        iterator.next();
      } else if(ch == '\n') {
        iterator.pushback();
        return;
      } else if(ch == endMarker || ch == '\n' || ch == '\r' || ch < 0) {
        return;
      }
    }
  }

  private Kind scanMultiLineComment(boolean start) {
    int ch = iterator.next();
    if(!start && ch == '\n') {
      return Kind.NEWLINE;
    }
    if(ch == '@') {
      iterator.advanceIdentifier();
      return Kind.COMMENT_TAG;
    }
    for(;;) {
      if(ch < 0 || (ch == '*' && iterator.peek() == '/')) {
        iterator.next();
        inMultiLineComment = false;
        return Kind.COMMENT;
      }
      if(ch == '\n') {
        iterator.pushback();
        return Kind.COMMENT;
      }
      if(ch == '@') {
        iterator.pushback();
        return Kind.COMMENT;
      }
      ch = iterator.next();
    }
  }

  private Kind scanNormal(int ch) {
    for(;;) {
      switch(ch) {
        case '\n':
        case '\"':
        case '\'':
        case CharacterIterator.EOF:
          iterator.pushback();
          return Kind.NORMAL;
        case '/':
          if(iterator.check("/*")) {
            iterator.pushback();
            return Kind.NORMAL;
          }
          break;
        default:
          if(Character.isJavaIdentifierStart(ch)) {
            iterator.setMarker(true);
            iterator.advanceIdentifier();
            if(iterator.isKeyword(KEYWORD_LIST)) {
              if(iterator.isMarkerAtStart()) {
                return Kind.KEYWORD;
              }
              iterator.rewindToMarker();
              return Kind.NORMAL;
            } else if(iterator.isKeyword(SPECIAL_KEYWORD_LIST)) {
              if(iterator.isMarkerAtStart()) {
                return Kind.SPECIAL_KEYWORD;
              }
              iterator.rewindToMarker();
              return Kind.NORMAL;
            } else if (Character.isDigit(ch)) {
              return Kind.NUMBER;
            }
          }
          break;
      }
      ch = iterator.next();
    }
  }

}