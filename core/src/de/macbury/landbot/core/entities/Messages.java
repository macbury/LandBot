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
  public void dispatchInFutureMessage(Telegraph sender, TelegramEvents event, Object payload) {
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
   * Alias to {@link MessageDispatcher#addListener(Telegraph, int)}
   * @param listener
   * @param events
   */
  public void addListeners(Telegraph listener, TelegramEvents ...events) {
    for (int i = 0; i < events.length; i++) {
      addListener(listener, events[i].ordinal());
    }
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
   * Alias to {@link MessageDispatcher#removeListener(Telegraph, int)}
   * @param listener
   * @param events
   */
  public void removeListeners(Telegraph listener, TelegramEvents ...events) {
    for (int i = 0; i < events.length; i++) {
      removeListener(listener, events[i].ordinal());
    }
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

  /**
   * Sends {@link TelegramEvents#RunScript} information to {@link de.macbury.landbot.core.entities.systems.LanderCPUSystem}
   * @param entity
   */
  public void dispatchRunScript(Entity entity) {
    dispatchMessage(Components.Position.get(entity), TelegramEvents.RunScript.ordinal());
  }

  /**
   * Sends {@link TelegramEvents#StopScript} information to {@link de.macbury.landbot.core.entities.systems.LanderCPUSystem}
   * @param entity
   */
  public void dispatchStopScript(Entity entity) {
    dispatchMessage(Components.Position.get(entity), TelegramEvents.StopScript.ordinal());
  }

  /**
   * Sends notification that script started execution
   * @param entity
   */
  public void dispatchOnScriptStart(Entity entity) {
    dispatchInFutureMessage(Components.Position.get(entity), TelegramEvents.OnScriptStart, null);
  }

  /**
   * Sends notification that script stoped execution
   * @param entity
   */
  public void dispatchOnScriptStop(Entity entity) {
    dispatchInFutureMessage(Components.Position.get(entity), TelegramEvents.OnScriptStop, null);
  }

  /**
   * Sends notification that script got exception on execution
   * @param entity
   */
  public void dispatchOnScriptError(Entity entity, Exception exception) {
    dispatchInFutureMessage(Components.Position.get(entity), TelegramEvents.OnScriptException, exception);
  }

  public void dispatchMessage(TelegramEvents telegramEvent, String payloadText) {
    dispatchMessage(telegramEvent.ordinal(), payloadText);
  }


  public void dispatchMessage(TelegramEvents telegramEvents) {
    dispatchMessage(telegramEvents.ordinal());
  }
}
