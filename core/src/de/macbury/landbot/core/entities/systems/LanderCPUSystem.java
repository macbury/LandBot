package de.macbury.landbot.core.entities.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.landbot.LandBot;
import de.macbury.landbot.core.TelegramEvents;
import de.macbury.landbot.core.WorldState;
import de.macbury.landbot.core.entities.Components;
import de.macbury.landbot.core.entities.Messages;
import de.macbury.landbot.core.entities.components.CPUComponent;
import de.macbury.landbot.game.scripting.EntityScriptEnv;
import de.macbury.landbot.core.scripting.IScriptListener;
import de.macbury.landbot.core.scripting.ScriptRunner;
import org.luaj.vm2.LuaError;

/**
 * This system updates each {@link CPUComponent}, programs it and listen for events
 */
public class LanderCPUSystem extends IteratingSystem implements Disposable, Telegraph, IScriptListener, EntityListener {
  private Messages messages;

  public LanderCPUSystem(LandBot game, WorldState worldState) {
    super(Family.all(CPUComponent.class).get());
    this.messages = game.messages;

    messages.addListeners(this, TelegramEvents.RunScript, TelegramEvents.StopScript);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Components.CPU.get(entity).update(deltaTime);
  }

  @Override
  public void dispose() {
    messages.removeListeners(this, TelegramEvents.RunScript, TelegramEvents.StopScript);
    messages = null;
  }

  /**
   * Loads script from {@link CPUComponent} into {@link ScriptRunner} and starts it
   * @param entity
   */
  private void runScript(Entity entity) {
    CPUComponent cpu = Components.CPU.get(entity);

    ScriptRunner scriptRunner = new ScriptRunner(cpu.getSource(), new EntityScriptEnv(entity, messages));
    scriptRunner.setListener(this);
    scriptRunner.setOwner(entity);
    cpu.setScriptRunner(scriptRunner);
    scriptRunner.start();
  }

  /**
   * Stops script in {@link CPUComponent} into {@link ScriptRunner} and starts it
   * @param entity
   */
  private void stopScript(Entity entity) {
    CPUComponent cpu          = Components.CPU.get(entity);
    ScriptRunner scriptRunner = cpu.getScriptRunner();
    if (scriptRunner != null) {
      scriptRunner.stop();
    }
  }

  @Override
  public boolean handleMessage(Telegram msg) {
    switch (TelegramEvents.from(msg)) {
      case RunScript:
        runScript(TelegramEvents.getEntity(msg.sender));
        return true;

      case StopScript:
        stopScript(TelegramEvents.getEntity(msg.sender));
        return false;
    }
    return false;
  }

  @Override
  public void onScriptStart(ScriptRunner runner) {
    messages.dispatchOnScriptStart(runner.getOwner());
  }

  @Override
  public void onScriptError(ScriptRunner runner, LuaError error) {
    messages.dispatchOnScriptError(runner.getOwner(), error);
    error.printStackTrace();
  }

  @Override
  public void onScriptStop(ScriptRunner runner) {
    Entity entity    = runner.getOwner();
    CPUComponent cpu = Components.CPU.get(entity);
    cpu.setScriptRunner(null);

    messages.dispatchOnScriptStop(entity);
  }

  @Override
  public void entityAdded(Entity entity) {

  }

  @Override
  public void entityRemoved(Entity entity) {
    if (getFamily().matches(entity)) {
      ScriptRunner scriptRunner = Components.CPU.get(entity).getScriptRunner();
      if (scriptRunner != null) {
        scriptRunner.stop();
        messages.dispatchOnScriptStop(entity);
      }
    }
  }
}
