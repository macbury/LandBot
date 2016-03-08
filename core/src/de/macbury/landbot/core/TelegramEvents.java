package de.macbury.landbot.core;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import de.macbury.landbot.core.entities.components.BaseFSMComponent;
import de.macbury.landbot.core.entities.components.PositionComponent;

/**
 * All events used in game!
 */
public enum TelegramEvents {
  /**
   * Inform {@link de.macbury.landbot.core.entities.components.CPUComponent} to start executing script
   */
  RunScript,
  /**
   * Inform {@link de.macbury.landbot.core.entities.components.CPUComponent} to stop executing script
   */
  StopScript,

  /**
   * These states are triggered by {@link de.macbury.landbot.core.entities.components.CPUComponent} after script execution changes
   */
  OnScriptStart,
  OnScriptException,
  OnScriptStop,

  UiShowTextCursor, UiShowNormalCursor,

  /**
   * This event is triggered by user and starts landing sequence
   */
  RunLandingScript,
  StopLandingScript;

  /**
   * Return true if {@link Telegram#message} equals this
   * @param telegram
   * @return
   */
  public boolean is(Telegram telegram) {
    return telegram.message == ordinal();
  }

  public static TelegramEvents from(Telegram msg) {
    return TelegramEvents.values()[msg.message];
  }

  /**
   * Retrive entity from {@link Telegraph}
   * @param sender
   * @return
   */
  public static Entity getEntity(Telegraph sender) {
    if (BaseFSMComponent.class.isInstance(sender)) {
      return ((BaseFSMComponent)sender).getEntity();
    } else {
      return ((PositionComponent)sender).entity;
    }
  }
}
