package de.macbury.landbot.game.scripting.libs;

import com.badlogic.gdx.Gdx;
import de.macbury.landbot.core.scripting.ScriptRunner;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * Base implementation for logging
 */
public class IOLib extends TwoArgFunction {
  private final ScriptRunner scriptRunner;

  public IOLib(ScriptRunner scriptRunner) {
    this.scriptRunner = scriptRunner;
  }

  public LuaValue call(LuaValue modname, LuaValue env) {
    env.set("print", new PrintFunction(scriptRunner));
    return null;
  }

  public class PrintFunction extends OneArgFunction {
    private static final String TAG = "PrintFunction";
    private final ScriptRunner scriptRunner;

    public PrintFunction(ScriptRunner scriptRunner) {
      this.scriptRunner = scriptRunner;
    }

    @Override
    public LuaValue call(LuaValue arg) {
      Gdx.app.log(TAG, arg.toString());
      Object result = scriptRunner.yield();
      Gdx.app.log(TAG, "Result of yield: " + result.toString());
      return null;
    }
  }
}
