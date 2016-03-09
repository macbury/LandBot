package de.macbury.landbot.core.entities.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.landbot.LandBot;
import de.macbury.landbot.core.WorldState;
import de.macbury.landbot.core.entities.Components;
import de.macbury.landbot.core.entities.components.BodyComponent;
import de.macbury.landbot.core.entities.components.CelestialBodyComponent;
import de.macbury.landbot.core.entities.components.PositionComponent;

/**
 * This system calculates all gravity pull for each {@link de.macbury.landbot.core.entities.components.CelestialBodyComponent} to other {@link BodyComponent}
 */
//http://mentalgrain.com/box2d/simulating-multiple-sources-of-gravity-in-box2d/
public class CelestialBodiesGravitySystem extends IteratingSystem implements Disposable {
  private static final float GRAVITY_LEN_MULTIPLICATOR = 4f;
  private static final String TAG = "CelestialBodiesGravitySystem";
  private WorldState worldState;
  private ImmutableArray<Entity> celestialBodiesEntities;
  private final Vector2 tempBodyDistance = new Vector2();
  public CelestialBodiesGravitySystem(LandBot game, WorldState worldState) {
    super(Family.all(BodyComponent.class).get());
    this.worldState = worldState;
  }

  @Override
  public void addedToEngine(Engine engine) {
    super.addedToEngine(engine);
    celestialBodiesEntities = engine.getEntitiesFor(Family.all(CelestialBodyComponent.class, PositionComponent.class).get());
  }

  @Override
  public void removedFromEngine(Engine engine) {
    super.removedFromEngine(engine);
    celestialBodiesEntities = null;
  }

  @Override
  public void update(float deltaTime) {
    worldState.world.clearForces();
    super.update(deltaTime);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    float force                       = 0;
    BodyComponent debrisBodyComponent = Components.Body.get(entity);

    for (Entity celestialBodyEntity : celestialBodiesEntities) {
      CelestialBodyComponent celestialBodyComponent = Components.CelestialBody.get(celestialBodyEntity);

      if (entity != celestialBodyEntity) {
        float celestialBodyRadius = Components.Body.get(celestialBodyEntity).getRadius();
        // Add the distance to the debris
        tempBodyDistance.set(Components.Body.get(entity).getPosition());
        // Subtract the distance to the planet's position
        // to get the vector between the debris and the planet.
        tempBodyDistance.sub(Components.Body.get(celestialBodyEntity).getPosition());

        // Calculate the magnitude of the force to apply to the debris.
        // This is proportional to the distance between the planet and
        // the debris. The force is weaker the further away the debris.
        // https://github.com/Fahien/spacefloat/blob/9bea9f477df65b1cf6d717edd802fc2dea13c68a/core/src/me/fahien/spacefloat/system/BulletSystem.java#L564
        float len = tempBodyDistance.len();
        force = (celestialBodyComponent.getGravitationalForce() * debrisBodyComponent.getMass()) / (len * GRAVITY_LEN_MULTIPLICATOR);

        if (len < celestialBodyRadius * celestialBodyComponent.getGravitationalFactor()) {
          // Change the direction of the vector so that the force will be
          // towards the planet.
          tempBodyDistance.scl(-force);
          debrisBodyComponent.body.applyForceToCenter(tempBodyDistance, true);
        }

        //Gdx.app.log(TAG, "Speed: " + Components.Body.get(entity).body.getLinearVelocity().len() + " m/s");
      }
    }
  }


  @Override
  public void dispose() {
    worldState = null;
  }

}
