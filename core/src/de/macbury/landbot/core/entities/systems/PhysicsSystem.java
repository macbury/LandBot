package de.macbury.landbot.core.entities.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.landbot.core.WorldState;
import de.macbury.landbot.core.entities.Components;
import de.macbury.landbot.core.entities.components.BodyComponent;
import de.macbury.landbot.core.entities.components.PositionComponent;

/**
 * This system handles adding entities with box2d components to {@link World}, simulate planet gravities and updateing {@link World}
 */
public class PhysicsSystem extends IteratingSystem implements Disposable, EntityListener {
  private World world;
  /**
   * Pixels per meter
   */
  public final static float PPM           = 20;
  public PhysicsSystem(WorldState worldState) {
    super(Family.all(PositionComponent.class, BodyComponent.class).get());
    this.world = worldState.world;
  }

  @Override
  public void update(float deltaTime) {
    super.update(deltaTime);
    world.step(1/60f, 6, 2);
  }

  /**
   * Update {@link PositionComponent} rotation and position using information from {@link BodyComponent}
   * @param entity
   * @param deltaTime
   */
  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    BodyComponent bodyComponent         = Components.Body.get(entity);
    PositionComponent positionComponent = Components.Position.get(entity);
    positionComponent.set(bodyComponent.body.getPosition());
    positionComponent.rotation          = bodyComponent.body.getAngle();
  }


  @Override
  public void dispose() {
    world = null;
  }

  /**
   * Bootstrap {@link com.badlogic.gdx.physics.box2d.Body} for {@link Entity} on entity add to world
   * @param entity
   */
  @Override
  public void entityAdded(Entity entity) {
    if (getFamily().matches(entity)) {
      BodyComponent bodyComponent         = Components.Body.get(entity);
      PositionComponent positionComponent = Components.Position.get(entity);

      BodyDef bdef = new BodyDef();
      bdef.position.set(positionComponent.x / PPM, positionComponent.y / PPM);
      bdef.type = BodyDef.BodyType.DynamicBody;
      bodyComponent.body      = world.createBody(bdef);
      PolygonShape shape = new PolygonShape();
      shape.setAsBox(10 / PPM, 10 / PPM);
      FixtureDef fdef = new FixtureDef();
      fdef.shape = shape;
      Fixture Fix = bodyComponent.body.createFixture(fdef);
      Fix.setRestitution(0.4f);
    }
  }

  /**
   * Dispose {@link com.badlogic.gdx.physics.box2d.Body} from {@link Entity} on entity removal
   * @param entity
   */
  @Override
  public void entityRemoved(Entity entity) {
    if (Components.Body.has(entity)) {
      world.destroyBody(Components.Body.get(entity).body);
    }
  }
}
