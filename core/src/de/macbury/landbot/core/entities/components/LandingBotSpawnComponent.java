package de.macbury.landbot.core.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Pool;
import de.macbury.landbot.core.assets.Assets;
import de.macbury.landbot.core.entities.Messages;
import de.macbury.landbot.core.entities.blueprint.ComponentBlueprint;

/**
 * This component will be used to spawn {@link Entity} landing bot
 */
public class LandingBotSpawnComponent implements Pool.Poolable, Component {
  @Override
  public void reset() {

  }

  public static class Blueprint extends ComponentBlueprint<LandingBotSpawnComponent> {
    @Override
    public void prepareDependencies(Array<AssetDescriptor> dependencies) {

    }

    @Override
    public void assignDependencies(Assets assets) {

    }

    @Override
    public void applyTo(LandingBotSpawnComponent component, Entity target, Messages messages) {

    }

    @Override
    public void load(JsonValue source, Json json) {

    }

    @Override
    public void save(Json target, LandingBotSpawnComponent source) {

    }

    @Override
    public void dispose() {

    }
  }
}
