package de.macbury.landbot.core.scripting;

import org.luaj.vm2.LuaError;

/**
 * Basic interface for tracking script execution
 */
public interface IScriptListener {
  /**
   * Triggered by script thread on start of script execution
   * @param runner
   */
  public void onScriptStart(ScriptRunner runner);
  /**
   * Triggered by script thread on exception before script stops
   * @param runner
   * @param error lua exception to handle
   */
  public void onScriptError(ScriptRunner runner, LuaError error);
  /**
   * Triggered after script is stopped by script thread
   * @param runner
   */
  public void onScriptStop(ScriptRunner runner);
}
