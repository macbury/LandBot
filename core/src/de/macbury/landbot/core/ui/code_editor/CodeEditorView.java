package de.macbury.landbot.core.ui.code_editor;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.landbot.core.entities.Messages;
import de.macbury.landbot.core.ui.code_editor.js.LuaScanner;
import de.macbury.landbot.core.ui.code_editor.widget.CodeEditorTextArea;

/**
 * Implementation of basic code editor
 */
public class CodeEditorView extends ScrollPane implements Disposable {
  private CodeEditorTextArea textArea;

  public CodeEditorView(CodeEditorTextArea.CodeEditorTextAreaStyle style, LuaScanner js, Messages messages) {
    super(null, style.scrollPaneStyle);

    this.setFadeScrollBars(false);
    this.setFlickScroll(false);

    this.textArea = new CodeEditorTextArea(style, this, js, messages);
    this.setWidget(textArea);
  }

  public CodeEditorView(Skin skin, LuaScanner js, Messages messages) {
    this(skin.get(CodeEditorTextArea.CodeEditorTextAreaStyle.class), js, messages);
  }

  public CodeEditorTextArea getTextArea() {
    return textArea;
  }

  public void setText(String text) {
    this.textArea.setText(text);
  }

  public void focus() {
    getStage().setScrollFocus(this);
    getStage().setKeyboardFocus(textArea);
    textArea.resetBlink();
  }

  public void unfocus() {
    getStage().unfocus(this);
    getStage().unfocus(textArea);
  }

  public String getText() {
    return textArea.getAllText();
  }

  @Override
  public void dispose() {
    textArea.dispose();
    unfocus();
  }
}
