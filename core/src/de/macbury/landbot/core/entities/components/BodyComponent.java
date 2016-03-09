package de.macbury.landbot.core.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.*;
import de.macbury.landbot.core.assets.Assets;
import de.macbury.landbot.core.entities.Messages;
import de.macbury.landbot.core.entities.blueprint.ComponentBlueprint;

/**
 * This class contains all information aboud box2d {@link Body}
 */
public class BodyComponent implements Pool.Poolable, Component {
  public Body body;
  public BodyDef bodyDef;
  public FixtureDef fixtureDef;

  @Override
  public void reset() {
    body = null;
    bodyDef = null;
    fixtureDef = null;
  }

  public float getMass() {
    return body.getMass();
  }

  public float getRadius() {
    return fixtureDef.shape.getRadius();
  }

  public Vector2 getPosition() {
    return body.getPosition();
  }

  public static class Blueprint extends ComponentBlueprint<BodyComponent> {
    private BodyDef.BodyType bodyType = BodyDef.BodyType.StaticBody;
    private Vector2 dimension = new Vector2(5,5);
    private float density;
    private float friction;
    private float restitution;
    private Shape shape;
    private float radius;
    private enum BlueprintShape {
      Box, Circle
    }

    private BlueprintShape blueprintShapeType;

    @Override
    public void prepareDependencies(Array<AssetDescriptor> dependencies) {

    }

    @Override
    public void assignDependencies(Assets assets) {

    }

    @Override
    public void applyTo(BodyComponent component, Entity owner, Messages messages) {
      component.bodyDef         = new BodyDef();
      component.bodyDef.type    = bodyType;
      component.fixtureDef      = new FixtureDef();
      component.fixtureDef.density = density;
      component.fixtureDef.friction = friction;
      component.fixtureDef.restitution = restitution;
      component.fixtureDef.shape = shape;
    }

    @Override
    public void load(JsonValue source, Json json) {
      bodyType      = json.readValue("type", BodyDef.BodyType.class, source);
      density       = json.readValue("density", Float.class, source);
      friction      = json.readValue("friction", Float.class, source);
      restitution   = json.readValue("restitution", Float.class, source);
      blueprintShapeType = json.readValue("shape", BlueprintShape.class, source);

      if (blueprintShapeType == BlueprintShape.Circle) {
        radius     = source.getFloat("radius");
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        this.shape = circleShape;
      } else if (blueprintShapeType == BlueprintShape.Box) {
        dimension     = json.readValue("dimension", Vector2.class, source);
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(dimension.x, dimension.y);
        this.shape = polygonShape;
      } else {
        throw new GdxRuntimeException("Unsuported shape type: " + blueprintShapeType + " of " + BlueprintShape.values());
      }
    }

    @Override
    public void save(Json target, BodyComponent source) {

    }

    @Override
    public void dispose() {
      shape.dispose();
    }
  }
}
