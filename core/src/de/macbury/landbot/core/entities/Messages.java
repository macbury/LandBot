package de.macbury.landbot.core.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegraph;
import de.macbury.landbot.core.TelegramEvents;
import de.macbury.landbot.core.entities.components.PositionComponent;

/**
 * Message dispatcher with nicer helper methods to send information
 */
public class Messages extends MessageDispatcher {

  /**
   * Dispatch message from entity
   * @param sender
   * @param event
   * @param payload
   */
  public void dispatchMessage(Telegraph sender, TelegramEvents event, Object payload) {
    synchronized (this) {
      dispatchMessage(0.001f, sender, event.ordinal(), payload);
    }
  }


  @Override
  public void update() {
    synchronized (this) {
      super.update();
    }
  }

  /**
   * Alias to {@link MessageDispatcher#addListener(Telegraph, int)}
   * @param listener
   * @param event
   */
  public void addListener(Telegraph listener, TelegramEvents event) {
    addListener(listener, event.ordinal());
  }

  /**
   * Alias to {@link MessageDispatcher#removeListener(Telegraph, int)}
   * @param listener
   * @param event
   */
  public void removeListener(Telegraph listener, TelegramEvents event) {
    removeListener(listener, event.ordinal());
  }

  /**
   * Sends telegram with {@link TelegramEvents#UiShowNormalCursor}. This will change cursor of the hud
   */
  public void dispatchShowNormalCursor() {
    dispatchMessage(TelegramEvents.UiShowNormalCursor.ordinal());
  }

  /**
   * Sends telegram with {@link TelegramEvents#UiShowTextCursor}. This will change cursor of the hud
   */
  public void dispatchShowTextCursor() {
    dispatchMessage(TelegramEvents.UiShowTextCursor.ordinal());
  }
}
