package de.macbury.landbot.core.entities.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.macbury.landbot.LandBot;
import de.macbury.landbot.core.TelegramEvents;
import de.macbury.landbot.core.WorldState;
import de.macbury.landbot.core.entities.Components;
import de.macbury.landbot.core.entities.Messages;

/**
 * This system contains all logic for reseting world and spawning new lander
 */
public class LandingSystem extends EntitySystem implements Disposable, EntityListener, Telegraph {
  private WorldState worldState;
  private Messages messages;
  /**
   * At this entity position, landing bot will spawn
   */
  private Entity spawningEntity;
  private Entity landingBotEntity;

  public LandingSystem(LandBot game, WorldState worldState) {
    this.messages = game.messages;
    messages.addListeners(this, TelegramEvents.RunLandingScript, TelegramEvents.StopLandingScript);
    this.worldState = worldState;
  }

  @Override
  public void dispose() {
    messages.removeListeners(this, TelegramEvents.RunLandingScript, TelegramEvents.StopLandingScript);
    spawningEntity = null;
    messages       = null;
    worldState = null;
  }

  @Override
  public void update(float deltaTime) {
    if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
      Components.CPU.get(landingBotEntity).getScriptRunner().resume("MY result!");
    }
  }

  @Override
  public void entityAdded(Entity entity) {
    if (Components.LandingBotSpawn.has(entity)) {
      if (this.spawningEntity != null)
        throw new GdxRuntimeException("There can be only one(LandingBotSpawn) entity!!!");
      this.spawningEntity = entity;
    }
  }

  @Override
  public void entityRemoved(Entity entity) {
    if (this.spawningEntity == entity) {
      this.spawningEntity = null;
    }
  }

  @Override
  public boolean handleMessage(Telegram msg) {
    switch (TelegramEvents.from(msg)) {
      case RunLandingScript:
        stopLanding();
        startLanding((String)msg.extraInfo);
        return true;
      case StopLandingScript:
        stopLanding();
        break;
    }
    return false;
  }

  private void stopLanding() {
    if (landingBotEntity != null) {
      worldState.entities.removeEntity(landingBotEntity);
    }
    landingBotEntity = null;
  }

  /**
   * Spawn new landing bot
   * @param code lua code to run
   */
  private void startLanding(String code) {
    this.landingBotEntity = worldState.landingBotBlueprint.createAndAdd(worldState.entities, messages);
    Components.CPU.get(landingBotEntity).setSource(code);
    Components.Position.get(landingBotEntity).set(Components.Position.get(spawningEntity));
    messages.dispatchRunScript(landingBotEntity);
  }
}
