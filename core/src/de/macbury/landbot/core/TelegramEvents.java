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
  ScriptStart,
  ScriptException,
  ScriptPause,
  ScriptAbort,
  ScriptStop,
  Test,//TODO remove

  SelectedEntity,
  DeselectedEntity,

  /**
   * This event is triggered when entity have collided with terrain!
   */
  CollidedWithTerrain,

  /**
   * Here are events triggered by {@link de.macbury.landbot.core.entities.states.RobotMotorState}.
   * They can be used for starting and stoping sound/animation etc
   */
  MotorMovementStart,
  MotorMovementStop,

  MotorTurnStart,
  MotorTurnStop,

  /**
   * Informs ui to use diffrent cursor
   */
  UiShowNormalCursor,
  UiShowTextCursor,

  /**
   * Reprogram robot.
   */
  RestartRobot,
  /**
   * Starts robot script, Triggered by player clicking on ui
   */
  StartRobot,
  /**
   * Stops robot script, Triggered by player clicking on ui
   */
  StopRobot,
  /**
   * Make robot turn, payload is int with degrees
   */
  InstructionTurn,
  /**
   * Make robot wait, payload is float with seconds
   */
  InstructionWait,
  /**
   * Start robot moving, payload is int with distance
   */
  InstructionMove,

  /**
   * Sends message from entity with payload
   */
  InstructionMessage;

  public static TelegramEvents RobotInstructionEvents[] = {
    InstructionMove,
    InstructionWait,
    InstructionTurn
  };

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
