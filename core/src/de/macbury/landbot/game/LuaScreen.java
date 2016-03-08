package de.macbury.landbot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import de.macbury.landbot.core.screens.ScreenBase;
import de.macbury.landbot.core.scripting.ScriptEnv;
import de.macbury.landbot.core.scripting.ScriptRunner;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.JsePlatform;

/**
 * http://stackoverflow.com/questions/17496868/lua-java-luaj-handling-or-interrupting-infinite-loops-and-threads
 */
public class LuaScreen extends ScreenBase {
  private ScriptRunner scriptRunner;
  private FPSLogger fpsLogger;

  @Override
  public void preload() {

  }

  @Override
  public void create() {
    this.fpsLogger    = new FPSLogger();
    //this.scriptRunner = new ScriptRunner(Gdx.files.internal("test.lua").readString(), new ScriptEnv());
    //scriptRunner.start();
  }

  @Override
  public void render(float delta) {
    fpsLogger.log();

    if (Gdx.input.isKeyPressed(Input.Keys.K)) {
      scriptRunner.stop();
    }

    scriptRunner.update(delta);
  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void dispose() {

  }

  public class ScriptEnforcer extends DebugLib {
    @Override
    public void onInstruction(int pc, Varargs v, int top) {
      super.onInstruction(pc, v, top);
    }
  }
}
