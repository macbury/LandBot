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
public class ScriptEnv extends Globals implements Disposable {
  public ScriptEnv() {
    super();

    load(new PackageLib());
    load(new TableLib());
    load(new StringLib());
    load(new JseMathLib());
    load(new IOLib());
  }

  @Override
  public void dispose() {

  }

  //TODO: This is placeholder remove this
  public class IOLib extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
      env.set("print", new PrintFunction());
      return null;
    }

    public class PrintFunction extends OneArgFunction {
      private static final String TAG = "PrintFunction";

      @Override
      public LuaValue call(LuaValue arg) {
        Gdx.app.log(TAG, arg.toString());
        return null;
      }
    }
  }
}
