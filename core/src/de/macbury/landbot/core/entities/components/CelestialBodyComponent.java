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
 * Describe planet, asteroid or any of celestial body
 */
public class CelestialBodyComponent implements Component, Pool.Poolable {
  /**
   * The gravitional force const
   */
  private float gravitationalForce;

  /**
   * Multiplied by radius to get the reach of the gravitational force.
   */
  private float gravitationalFactor;

  @Override
  public void reset() {
    gravitationalForce  = 0;
    gravitationalFactor = 0;
  }

  public float getGravitationalForce() {
    return gravitationalForce;
  }

  public float getGravitationalFactor() {
    return gravitationalFactor;
  }

  public static class Blueprint extends ComponentBlueprint<CelestialBodyComponent> {
    private float gravitionalForce;
    private float gravitionalFactor;

    @Override
    public void prepareDependencies(Array<AssetDescriptor> dependencies) {

    }

    @Override
    public void assignDependencies(Assets assets) {

    }

    @Override
    public void applyTo(CelestialBodyComponent component, Entity target, Messages messages) {
      component.gravitationalFactor = gravitionalFactor;
      component.gravitationalForce = gravitionalForce;
    }

    @Override
    public void load(JsonValue source, Json json) {
      gravitionalFactor  = source.getFloat("gravitationalFactor");
      gravitionalForce   = source.getFloat("gravitationalForce");
    }

    @Override
    public void save(Json target, CelestialBodyComponent source) {

    }

    @Override
    public void dispose() {

    }
  }
}
