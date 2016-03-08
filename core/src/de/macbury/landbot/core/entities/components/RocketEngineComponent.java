package de.macbury.landbot.core.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Pool;
import de.macbury.landbot.core.assets.Assets;
import de.macbury.landbot.core.entities.Components;
import de.macbury.landbot.core.entities.Messages;
import de.macbury.landbot.core.entities.blueprint.ComponentBlueprint;

/**
 * Lander rocket
 */
public class RocketEngineComponent implements Component, Pool.Poolable {
  public float leftThruster;
  public float rightThruster;
  public float fuel;
  public float maxFuel;
  public float forcePerFuel;
  public float fuelPerSecond;

  @Override
  public void reset() {
    leftThruster = 0;
    rightThruster = 0;
  }

  public static class Blueprint extends ComponentBlueprint<RocketEngineComponent> {

    @Override
    public void prepareDependencies(Array<AssetDescriptor> dependencies) {

    }

    @Override
    public void assignDependencies(Assets assets) {

    }

    @Override
    public void applyTo(RocketEngineComponent component, Entity target, Messages messages) {

    }

    @Override
    public void load(JsonValue source, Json json) {

    }

    @Override
    public void save(Json target, RocketEngineComponent source) {

    }

    @Override
    public void dispose() {

    }
  }
}
