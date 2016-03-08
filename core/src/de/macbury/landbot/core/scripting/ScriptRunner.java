package de.macbury.landbot.core.scripting;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.luaj.vm2.*;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;

/**
 * This class runs Lua script in separate thread, and throttle it execution speed
 */
public class ScriptRunner implements Disposable {
  private static final String TAG = "ScriptRunner";
  private final int INSTRUCTIONS_PER_MS = 16;
  private final float PSEUDO_CPU_TICK = 0.001f;
  private ScriptEnv env;
  private ScriptEnforcer scriptEnforcer;
  private float accumulator;
  private float instructionsLeft;
  private final String source;
  private ScriptThread thread;
  private IScriptListener listener;
  private Entity owner;
  private boolean pause;
  private Object yieldResult;

  /**
   * Creates new script runner
   * @param source code to run
   * @param env the env of script
   */
  public ScriptRunner(String source, ScriptEnv env) {
    instructionsLeft = 0;
    this.env = env;
    this.source      = source;

    scriptEnforcer = new ScriptEnforcer();
    env.load(scriptEnforcer);
    LoadState.install(env);
    LuaC.install(env);

    env.setup(this);
  }

  public IScriptListener getListener() {
    return listener;
  }

  public void setListener(IScriptListener listener) {
    this.listener = listener;
  }

  /**
   * Wakes up sleeping script thread with result
   * @param result
   */
  public void resume(Object result) {
    if (yieldResult != null) {
      throw new GdxRuntimeException("There is already pending yieldResult:" + yieldResult);
    }
    yieldResult = result;
    this.pause = false;
    synchronized (thread){
      this.thread.notify();
    }
  }

  /**
   * Pause running script. Mainly should be used by lua scripts to stop script execution
   * @return object passed through {@link ScriptRunner#resume(Object)}
   */
  public Object yield() {
    synchronized (thread) {
      this.pause = true;
      try {
        thread.wait();
        Object res = yieldResult;
        yieldResult = null;
        return res;
      } catch (InterruptedException e) {
        throw new ScriptInterruptException();
      }
    }
  }

  public boolean isPaused() {
    return pause;
  }

  /**
   * Update current script execution. This is needed to script to be executed
   * @param delta
   */
  public void update(float delta) {
    if (!isPaused() && isRunning()) {
      this.accumulator += delta;
      while(accumulator > PSEUDO_CPU_TICK) {
        instructionsLeft +=  INSTRUCTIONS_PER_MS;
        accumulator-=PSEUDO_CPU_TICK;
      }

      if (instructionsLeft > 0) {
        synchronized (thread) {
          thread.notify();
        }
      }
    }
  }

  /**
   * Starts script in separate thread
   */
  public void start() {
    if (!isRunning()) {
      this.thread = new ScriptThread();

      if (listener != null) {
        synchronized (listener) {
          listener.onScriptStart(this);
        }
      }

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

  /**
   * Interputs current thread and force it to stop script
   */
  public void stop() {
    if (isRunning()) {
      instructionsLeft = 0;
      thread.interrupt();
      thread = null;
      pause = false;
      yieldResult = null;
    }
  }

  @Override
  public void dispose() {
    stop();

    if (env != null)
      env.dispose();
    env = null;

    setListener(null);
    owner = null;
  }

  /**
   * Who owns entity
   * @param owner
   */
  public void setOwner(Entity owner) {
    this.owner = owner;
  }

  public Entity getOwner() {
    return owner;
  }

  /**
   * Throttle script instruction rate
   */
  private class ScriptEnforcer extends DebugLib {
    /**
     * Throttle execution speed of script to emulate slower processor
     * @param pc
     * @param v
     * @param top
     */
    @Override
    public void onInstruction(int pc, Varargs v, int top) {
      synchronized (this) {
        while(instructionsLeft == 0) {
          try {
            thread.wait();
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
      try {
        LuaValue chunk = env.load(source);
        chunk.call();
      } catch (LuaError e) {
        if (ScriptInterruptException.class.isInstance(e.getCause())) {
          // Ignore this error
          Gdx.app.debug(TAG, "Somebody wants this script to stop. Forcing it");
        } else {
          if (listener != null) {
            synchronized (listener) {
              listener.onScriptError(ScriptRunner.this, e);
            }
          } else {
            Gdx.app.log(TAG, e.getMessage());
          }
        }
      } finally {
        if (listener != null) {
          synchronized (listener) {
            listener.onScriptStop(ScriptRunner.this);
          }
        }

        dispose();
      }
    }
  }

  /**
   * This exception is throwed to stop the script from running
   */
  private class ScriptInterruptException extends RuntimeException {

  }

}
