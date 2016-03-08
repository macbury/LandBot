package de.macbury.landbot.core.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.utils.*;
import de.macbury.landbot.core.assets.Assets;
import de.macbury.landbot.core.entities.Messages;
import de.macbury.landbot.core.entities.blueprint.ComponentBlueprint;
import de.macbury.landbot.core.scripting.ScriptEnv;
import de.macbury.landbot.core.scripting.ScriptRunner;

/**
 * This component contains {@link ScriptRunner} and source script
 */
public class CPUComponent implements Pool.Poolable, Component {
  private ScriptRunner scriptRunner;
  private String source;

  public ScriptRunner getScriptRunner() {
    return scriptRunner;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public void update(float delta) {
    if (scriptRunner != null)
      scriptRunner.update(delta);
  }

  @Override
  public void reset() {
    if (scriptRunner != null)
      scriptRunner.dispose();
    scriptRunner = null;
  }

  /**
   * Dispose old script runner and set an new one
   * @param scriptRunner
   */
  public void setScriptRunner(ScriptRunner scriptRunner) {
    if (this.scriptRunner != null)
      this.scriptRunner.dispose();
    this.scriptRunner = scriptRunner;
  }


  public static class Blueprint extends ComponentBlueprint<CPUComponent> {
    @Override
    public void prepareDependencies(Array<AssetDescriptor> dependencies) {

    }

    @Override
    public void assignDependencies(Assets assets) {

    }

    @Override
    public void applyTo(CPUComponent component, Entity target, Messages messages) {

    }

    @Override
    public void load(JsonValue source, Json json) {

    }

    @Override
    public void save(Json target, CPUComponent source) {

    }

    @Override
    public void dispose() {

    }
  }
}
