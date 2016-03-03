package de.macbury.landbot.core.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.Pool;
import de.macbury.landbot.core.TelegramEvents;
import de.macbury.landbot.core.entities.Messages;

/**
 * This component contains instance of {@link DefaultStateMachine}
 */
public abstract class BaseFSMComponent<S extends State<Entity>> implements Component, Pool.Poolable, Telegraph {
  protected DefaultStateMachine<Entity, S> stateMachine;
  protected Messages messages;

  /**
   * Creates state machine for entity
   * @param entity
   * @param messages
   * @return
   */
  public BaseFSMComponent init(Entity entity, Messages messages, S globalState, S initialState) {
    stateMachine = new DefaultStateMachine<Entity, S>(entity);
    stateMachine.setGlobalState(globalState);
    stateMachine.setInitialState(initialState);
    this.messages  = messages;
    return this;
  }

  /**
   * Alias for {@link Messages#dispatchMessage(Telegraph, int)} where telegraph is current component
   * @param msg
   */
  public void dispatchMessage(TelegramEvents msg) {
    getMessages().dispatchMessage(this, msg.ordinal());
  }

  /**
   * Update state machine
   */
  public void update () {
    stateMachine.update();
  }

  public void changeState (S state) {
    stateMachine.changeState(state);
  }

  public S getState() {
    return stateMachine.getCurrentState();
  }

  public boolean is(S state) {
    return getState() == state;
  }

  public StateMachine<Entity, S> getStateMachine () {
    return stateMachine;
  }

  @Override
  public boolean handleMessage (Telegram msg) {
    return stateMachine.handleMessage(msg);
  }

  @Override
  public void reset() {
    if (stateMachine != null)
      stateMachine.setOwner(null);
    stateMachine = null;
    messages = null;
  }

  public Messages getMessages() {
    return messages;
  }

  public Entity getEntity() {
    return stateMachine.getOwner();
  }

}