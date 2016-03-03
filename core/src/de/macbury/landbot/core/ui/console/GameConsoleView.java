package de.macbury.landbot.core.ui.console;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import de.macbury.landbot.core.TelegramEvents;
import de.macbury.landbot.core.entities.Components;
import de.macbury.landbot.core.entities.Messages;

/**
 * This widget displays all messages sended by robots
 */
//TODO add pooling for labels
public class GameConsoleView extends Table implements Disposable, Telegraph {
  public final static int MAX_LOG_LINES = 100;
  public static final String TAG = "GameConsoleView";
  private final Array<ConsoleLabel> labels;
  private final ScrollPane scroll;
  private Skin skin;
  private Messages messages;
  private Array<String> commandsHistory;
  private final VerticalGroup logEntries;
  private final TextField input;
  private boolean visible;
  private boolean cursorIsCatched;
  private int currentCommandIndex = 0;

  private Pool<ConsoleLabel> labelPool;

  public GameConsoleView(Messages messages, final Skin skin) {
    super();
    this.skin            = skin;
    this.messages        = messages;
    this.commandsHistory = new Array<String>();
    this.visible = false;
    setBackground(skin.getDrawable("console_bg"));
    setDebug(false);
    pad(4);

    labelPool = new Pool<ConsoleLabel>() {
      @Override
      protected ConsoleLabel newObject() {
        return new ConsoleLabel(skin);
      }
    };

    labels     = new Array<ConsoleLabel>();
    logEntries = new VerticalGroup();

    input = new TextField("Sample input!", skin, "console");
    this.setTouchable(Touchable.childrenOnly);
    /*input.addListener(new InputListener() {
      @Override
      public boolean keyUp(InputEvent event, int keycode) {
        if (keycode == Input.Keys.ENTER) {
          processCommand();
        } else if (keycode == Input.Keys.UP) {
          moveCommandUp();
        } else if (keycode == Input.Keys.DOWN) {
          moveCommandDown();
        }
        return super.keyUp(event, keycode);
      }
    });*/

    scroll = new ScrollPane(logEntries, skin, "console");
    scroll.setFadeScrollBars(false);
    scroll.setScrollbarsOnTop(false);
    scroll.setOverscroll(false, false);

    this.add(scroll).left().expand().fill().pad(4).row();

    //this.add(input).expandX().fillX().pad(4);

    messages.addListener(this, TelegramEvents.InstructionMessage);
    messages.addListener(this, TelegramEvents.ScriptException);
  }

  private void moveCommandDown() {
    currentCommandIndex++;
    currentCommandIndex = Math.min(currentCommandIndex, commandsHistory.size - 1);
    showCommandForCurrentIndex();
  }

  private void showCommandForCurrentIndex() {
    if (currentCommandIndex > 0 && currentCommandIndex < commandsHistory.size) {
      String command = commandsHistory.get(currentCommandIndex);
      if (command != null) {
        input.setText(command);
        input.setCursorPosition(command.length());
      }
    }

  }

  private void moveCommandUp() {
    currentCommandIndex--;
    currentCommandIndex = Math.max(currentCommandIndex, 0);
    showCommandForCurrentIndex();
  }

  private void processCommand() {
    String currentCommand      = input.getText();
    commandsHistory.add(currentCommand);
    input.setText("");
    currentCommandIndex        = commandsHistory.size;
  }

  public void toggle() {
    if (visible) {
      hide();
    } else {
      show();
    }
  }

  public boolean isEnabled() {
    return visible;
  }

  public void show() {
    visible = true;
    setColor(Color.BLACK);
    setZIndex(100);
    input.setText("");
  }

  public void hide() {
    visible = false;
    remove();
  }

  @Override
  public void dispose() {
    messages.removeListener(this, TelegramEvents.ScriptException);
    messages.removeListener(this, TelegramEvents.InstructionMessage);
    labelPool.freeAll(labels);
    labels.clear();
    messages = null;
    skin     = null;
  }

  /**
   * Removes oldest logs
   */
  private void trimLogs() {
    int diff = labels.size - MAX_LOG_LINES;
    if (diff > 0) {
      for (int i = 0; i < diff; i++) {
        labelPool.free(labels.removeIndex(0));
      }
    }
  }

  private ConsoleLabel pushLabel() {
    ConsoleLabel label = labelPool.obtain();
    logEntries.left().addActor(label);
    labels.add(label);

    scroll.validate();
    scroll.setScrollPercentY(1);
    return label;
  }

  @Override
  public boolean handleMessage(Telegram msg) {
    switch (TelegramEvents.from(msg)) {
      case InstructionMessage:
        trimLogs();
        pushLabel().set(msg, messages);
        return true;

      case ScriptException:
        trimLogs();
        pushLabel().set(msg, messages);
        return true;
    }

    return false;
  }

  /**
   * Simple label wrapper for incoming messages
   */
  public class ConsoleLabel extends Label implements Pool.Poolable {
    private Messages messages;
    private Entity entity;

    public ConsoleLabel(Skin skin) {
      super("", skin, "console");
      addCaptureListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {

        }
      });
    }

    /**
     * Copy information from telegram and display it as label
     * @param telegram
     * @return
     */
    public ConsoleLabel set(Telegram telegram, Messages messages) {
      this.messages = messages;
      this.entity   = TelegramEvents.getEntity(telegram.sender);
      //RobotCPUComponent cpu = Components.RobotCPU.get(entity);

      String message = "";

      if (Exception.class.isInstance(telegram.extraInfo)) {
        Exception exception = (Exception)telegram.extraInfo;
        message = exception.getMessage();
      } else {
        message = telegram.extraInfo.toString();
      }

     // setText("[" + cpu.getName()+"] " + message);
      return this;
    }

    @Override
    public void reset() {
      entity   = null;
      messages = null;
      setText("");
      remove();
    }
  }
}