package de.macbury.landbot.core.scripting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.JseMathLib;

/**
 * This are basic env for lander
 */
public abstract class ScriptEnv extends Globals implements Disposable {
  public ScriptEnv() {
    super();

    load(new PackageLib());
    load(new TableLib());
    load(new StringLib());
    load(new JseMathLib());
  }

  public abstract void setup(ScriptRunner runner);
}
