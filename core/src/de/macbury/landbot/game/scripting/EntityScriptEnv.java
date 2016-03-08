package de.macbury.landbot.game.scripting;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import de.macbury.landbot.core.entities.Messages;
import de.macbury.landbot.core.scripting.ScriptEnv;
import de.macbury.landbot.core.scripting.ScriptRunner;
import de.macbury.landbot.game.scripting.libs.IOLib;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * Analyzes entity end creates proper env for script
 */
public class EntityScriptEnv extends ScriptEnv {
  private Messages messages;
  private Entity entity;
  private ScriptRunner scriptRunner;

  public EntityScriptEnv(Entity entity, Messages messages) {
    super();
    this.entity   = entity;
    this.messages = messages;
  }

  @Override
  public void dispose() {
    entity = null;
    messages = null;
    scriptRunner = null;
  }

  @Override
  public void setup(ScriptRunner runner) {
    this.scriptRunner = runner;
    load(new IOLib(scriptRunner));
  }

}
