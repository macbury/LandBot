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
import de.macbury.landbot.core.entities.Components;
import de.macbury.landbot.core.entities.Messages;
import de.macbury.landbot.core.ui.code_editor.CodeEditorView;
import de.macbury.landbot.core.ui.code_editor.js.JavaScriptScanner;
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

  public ProgramingController(Messages messages, GameplayScreenGroup gameplayScreenGroup, Skin skin) {
    this.group    = gameplayScreenGroup;
    this.messages = messages;
    messages.addListener(this, TelegramEvents.SelectedEntity);
    messages.addListener(this, TelegramEvents.DeselectedEntity);
    messages.addListener(this, TelegramEvents.ScriptStart);
    messages.addListener(this, TelegramEvents.ScriptStop);

    this.codeEditorWindow = new Dialog("Code editor", skin);
    codeEditorWindow.setSize(800, 600);
    codeEditorWindow.setResizable(true);
    codeEditorWindow.setVisible(false);
    codeEditorWindow.setModal(false);
    this.codeTextField     = new CodeEditorView(skin, new JavaScriptScanner(), messages);

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
    if (currentEntity != null) {
      //Gdx.app.log(TAG, "runStopButtonClick: " + Components.RobotCPU.get(currentEntity).getState());
      /*if (Components.RobotCPU.get(currentEntity).isRunning()) {
        stopCode();
      } else {
        runCode();
      }*/
    }
  }

  private void stopCode() {
    if (currentEntity != null) {
      //messages.dispatchStopRobot(currentEntity);
    }
  }

  private void runCode() {
    if (currentEntity != null) {
      //Components.RobotCPU.get(currentEntity).setSource(codeTextField.getText());
     // messages.dispatchReprogramRobot(currentEntity);
    }
  }

  @Override
  public void dispose() {
    currentEntity = null;
    messages.removeListener(this, TelegramEvents.SelectedEntity);
    messages.removeListener(this, TelegramEvents.DeselectedEntity);
    messages.removeListener(this, TelegramEvents.ScriptStart);
    messages.removeListener(this, TelegramEvents.ScriptStop);
    messages = null;
  }

  @Override
  public boolean handleMessage(Telegram msg) {
    Gdx.app.log(TAG, "handleMessage: " + TelegramEvents.from(msg));
    switch (TelegramEvents.from(msg)) {
      case ScriptStop:
        updateButtonByEntityState();
        return true;
      case ScriptStart:
        updateButtonByEntityState();
        return true;

      case SelectedEntity:
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

    }
    return false;
  }

  private void updateButtonByEntityState() {
    if (currentEntity != null) {
      //Gdx.app.log(TAG, "updateButtonByEntityState: " + Components.RobotCPU.get(currentEntity).getState());
      /*if (Components.RobotCPU.get(currentEntity).isRunning()) {
        runStopButton.setStyle(stopButtonStyle);
      } else {
        runStopButton.setStyle(runButtonStyle);
      }*/
    }
  }
}
