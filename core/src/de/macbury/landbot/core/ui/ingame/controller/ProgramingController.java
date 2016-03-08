package de.macbury.landbot.core.ui.ingame.controller;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.landbot.core.TelegramEvents;
import de.macbury.landbot.core.entities.Messages;
import de.macbury.landbot.core.ui.code_editor.CodeEditorView;
import de.macbury.landbot.core.ui.code_editor.js.LuaScanner;
import de.macbury.landbot.core.ui.ingame.GameplayScreenGroup;

/**
 * Created on 19.02.16.
 */
public class ProgramingController implements Disposable, Telegraph {
  private static final String TAG = "ProgramingController";
  private ImageButton.ImageButtonStyle stopButtonStyle;
  private ImageButton.ImageButtonStyle runButtonStyle;
  private ImageButton runStopButton;

  private CodeEditorView codeTextField;
  private Dialog codeEditorWindow;
  private GameplayScreenGroup group;
  private Messages messages;
  private Entity currentEntity;
  private boolean scriptRunning;

  public ProgramingController(Messages messages, GameplayScreenGroup gameplayScreenGroup, Skin skin) {
    this.group    = gameplayScreenGroup;
    this.messages = messages;
    messages.addListeners(this, TelegramEvents.OnScriptStart, TelegramEvents.OnScriptStop);
    //messages.addListener(this, TelegramEvents.SelectedEntity);
    //messages.addListener(this, TelegramEvents.DeselectedEntity);
    //messages.addListener(this, TelegramEvents.ScriptStart);
    //messages.addListener(this, TelegramEvents.ScriptStop);

    this.codeEditorWindow = new Dialog("Code editor", skin);
    codeEditorWindow.setSize(800, 600);
    codeEditorWindow.setResizable(true);
    codeEditorWindow.setVisible(false);
    codeEditorWindow.setModal(false);
    this.codeTextField     = new CodeEditorView(skin, new LuaScanner(), messages);

    runButtonStyle         = skin.get("run", ImageButton.ImageButtonStyle.class);
    stopButtonStyle        = skin.get("stop", ImageButton.ImageButtonStyle.class);
    this.runStopButton     = new ImageButton(skin, "run");
    runStopButton.setStyle(runButtonStyle);
    runStopButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        runStopButtonClick();
      }
    });

    Table contentTable = codeEditorWindow.getContentTable();

    contentTable.row(); {
      contentTable.add(runStopButton);
      contentTable.add().fillX().expandX();
    }

    contentTable.row(); {
      contentTable.add(codeTextField).fill().expand().colspan(3);
    }

    gameplayScreenGroup.addActor(codeEditorWindow);

  }

  private void runStopButtonClick() {
    runCode();
  }

  private void stopCode() {
    if (currentEntity != null) {

    }
  }

  private void runCode() {
    if (scriptRunning) {
      messages.dispatchMessage(TelegramEvents.StopLandingScript);
    } else {
      messages.dispatchMessage(TelegramEvents.RunLandingScript, codeTextField.getText());
    }

  }

  @Override
  public void dispose() {
    currentEntity = null;
    messages.removeListeners(this, TelegramEvents.OnScriptStart, TelegramEvents.OnScriptStop);
    messages = null;
  }

  @Override
  public boolean handleMessage(Telegram msg) {
    Gdx.app.log(TAG, "handleMessage: " + TelegramEvents.from(msg));
    switch (TelegramEvents.from(msg)) {
      case OnScriptStop:
        this.scriptRunning = false;
        updateButtonByEntityState();
        return true;
      case OnScriptStart:
        this.scriptRunning = true;
        updateButtonByEntityState();
        return true;

      /*case SelectedEntity:
        currentEntity = TelegramEvents.getEntity(msg.sender);

        String source = ""; //Components.RobotCPU.get(currentEntity).getSource();
        if (source == null)
          source = "";

        codeTextField.setText(source);
        updateButtonByEntityState();
        codeEditorWindow.setVisible(true);
        codeEditorWindow.toFront();
        codeTextField.focus();
        return true;

      case DeselectedEntity:
        currentEntity = null;
        codeEditorWindow.setVisible(false);
        return true;
*/
    }
    return false;
  }

  public void show() {
    codeEditorWindow.setVisible(true);
    codeEditorWindow.toFront();
    codeTextField.focus();
  }

  private void updateButtonByEntityState() {
    if (scriptRunning) {
      runStopButton.setStyle(stopButtonStyle);
    } else {
      runStopButton.setStyle(runButtonStyle);
    }
  }
}
