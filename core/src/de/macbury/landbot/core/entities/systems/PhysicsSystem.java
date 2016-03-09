package de.macbury.landbot.core.entities.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.landbot.LandBot;
import de.macbury.landbot.core.WorldState;
import de.macbury.landbot.core.entities.Components;
import de.macbury.landbot.core.entities.components.BodyComponent;
import de.macbury.landbot.core.entities.components.PositionComponent;
import de.macbury.landbot.core.graphics.framebuffer.Fbo;

/**
 * This system handles adding entities with box2d components to {@link World}, simulate planet gravities and updateing {@link World}
 */
public class PhysicsSystem extends IteratingSystem implements Disposable, EntityListener {
  private final Box2DDebugRenderer b2dr;
  private OrthographicCamera b2dCamera;
  private LandBot game;
  private World world;

  public PhysicsSystem(LandBot game, WorldState worldState) {
    super(Family.all(PositionComponent.class, BodyComponent.class).get());
    this.world      = worldState.world;
    this.b2dr       = new Box2DDebugRenderer();
    this.game       = game;
    this.b2dCamera  = worldState.b2dCamera;

    b2dr.setDrawVelocities(true);
    b2dr.setDrawContacts(true);
  }

  @Override
  public void update(float deltaTime) {
    world.step(1/60f, 10, 10);
    super.update(deltaTime);

    b2dCamera.update();
    game.fb.begin(Fbo.FinalResult); {
      b2dr.render(world, b2dCamera.combined);
    } game.fb.end();
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
    positionComponent.set(bodyComponent.body.getWorldCenter()).scl(WorldState.PPM);
    positionComponent.rotation          = bodyComponent.body.getAngle();
  }


  @Override
  public void dispose() {
    b2dr.dispose();
    world = null;
    game = null;
    b2dCamera = null;
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

      bodyComponent.bodyDef.position.set(positionComponent.x / WorldState.PPM, positionComponent.y / WorldState.PPM);
      bodyComponent.body = world.createBody(bodyComponent.bodyDef);
      bodyComponent.body.createFixture(bodyComponent.fixtureDef);
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
