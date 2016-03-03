package de.macbury.landbot.core.scripting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.luaj.vm2.*;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.JseMathLib;

/**
 * Created on 01.03.16.
 */
public class ScriptRunner implements Disposable {
  private static final String TAG = "ScriptRunner";
  private final int INSTRUCTIONS_PER_MS = 16;
  private final float PSEUDO_CPU_TICK = 0.001f;
  private Globals mainGlobals;
  private ScriptEnforcer scriptEnforcer;
  private float accumulator;
  private float instructionsLeft;
  private ScriptThread thread;

  public ScriptRunner() {
    instructionsLeft = 0;
  }

  /**
   * Creates whole env for current script
   */
  private void buildLuaEnv() {
    this.mainGlobals = new Globals();
    mainGlobals.load(new PackageLib());
    mainGlobals.load(new TableLib());
    mainGlobals.load(new StringLib());
    mainGlobals.load(new JseMathLib());
    mainGlobals.load(new IOLib());

    scriptEnforcer = new ScriptEnforcer();
    mainGlobals.load(scriptEnforcer);
    LoadState.install(mainGlobals);
    LuaC.install(mainGlobals);
  }

  public void update(float delta) {
    if (isRunning()) {
      this.accumulator += delta;
      while(accumulator > PSEUDO_CPU_TICK) {
        instructionsLeft +=  INSTRUCTIONS_PER_MS;
        accumulator-=PSEUDO_CPU_TICK;
      }

      if (instructionsLeft > 0) {
        synchronized (scriptEnforcer) {
          scriptEnforcer.notify();
        }
      }
    }
  }

  /**
   * Starts script in separate thread
   */
  public void start() {
    if (!isRunning()) {
      buildLuaEnv();
      this.thread = new ScriptThread();
      thread.start();
    } else {
      throw new GdxRuntimeException("Already running!");
    }
  }

  /**
   * Return true if script is executed
   * @return
   */
  public boolean isRunning() {
    return thread != null && thread.isAlive();
  }

  public void stop() {
    if (isRunning()) {
      instructionsLeft = 0;
      thread.interrupt();
      thread = null;
    }
  }

  @Override
  public void dispose() {
    stop();
  }

  /**
   * Throttle script instruction rate
   */
  private class ScriptEnforcer extends DebugLib {
    @Override
    public void onInstruction(int pc, Varargs v, int top) {
      synchronized (this) {
        while(instructionsLeft == 0) {
          try {
            wait();
          } catch (InterruptedException e) {
            /**
             * This will be catched in {@link ScriptThread#run()}
             */
            throw new ScriptInterruptException();
          }
        }

        super.onInstruction(pc, v, top);
        instructionsLeft--;
      }
    }
  }

  /**
   * Thread in which script is running
   */
  private class ScriptThread extends Thread {

    @Override
    public void run() {
      LuaValue chunk = mainGlobals.load(Gdx.files.internal("test.lua").readString());
      try {
        chunk.call();
      } catch (LuaError e) {
        if (ScriptInterruptException.class.isInstance(e.getCause())) {
          // Ignore this error
          Gdx.app.debug(TAG, "Somebody wants this script to stop. Forcing it");
        } else {
          // Real error here
          Gdx.app.log(TAG, e.getMessage());
        }
      }

    }
  }

  /**
   * This exception is throwed to stop the script from running
   */
  private class ScriptInterruptException extends RuntimeException {

  }

  public class IOLib extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
      env.set("print", new PrintFunction());
      return null;
    }

    public class PrintFunction extends OneArgFunction {
      @Override
      public LuaValue call(LuaValue arg) {
        Gdx.app.log(TAG, arg.toString());
        return null;
      }
    }
  }
}
