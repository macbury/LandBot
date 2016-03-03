package de.macbury.landbot.core.entities.components;
import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Pool;
import de.macbury.landbot.core.assets.Assets;
import de.macbury.landbot.core.entities.Messages;
import de.macbury.landbot.core.entities.blueprint.ComponentBlueprint;

/**
 * This class contains information about entity position on Screen, and if this entity is visible on current screen;
 */
public class PositionComponent extends Vector2 implements Component, Pool.Poolable, Telegraph {
  /**
   * Rotation in radians
   */
  public float rotation;
  public Entity entity;
  public Vector3 dimension = new Vector3();

  @Override
  public void reset() {
    setZero();
    rotation = 0;
    entity = null;
    dimension.set(1,1,1);
  }

  @Override
  public boolean handleMessage(Telegram msg) {
    return false;
  }

  public static class Blueprint extends ComponentBlueprint<PositionComponent> {
    public float x;
    public float y;
    public float z;
    public float rotation;

    @Override
    public void prepareDependencies(Array<AssetDescriptor> dependencies) {

    }

    @Override
    public void assignDependencies(Assets assets) {

    }

    @Override
    public void applyTo(PositionComponent component, Entity owner, Messages messages) {
      component.set(x, y);
      component.rotation = rotation;
    }

    @Override
    public void load(JsonValue source, Json json) {
      x = source.getFloat("x", 0);
      y = source.getFloat("y", 0);
      rotation = source.getFloat("rotation", 0);
    }

    @Override
    public void save(Json target, PositionComponent source) {
      target.writeValue("x", source.x);
      target.writeValue("y", source.y);
      target.writeValue("z", source.x);
      target.writeValue("rotation", source.rotation);
    }

    @Override
    public void dispose() {

    }
  }
}