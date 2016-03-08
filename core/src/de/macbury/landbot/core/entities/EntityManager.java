package de.macbury.landbot.core.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.landbot.LandBot;
import de.macbury.landbot.core.WorldState;
import de.macbury.landbot.core.entities.systems.*;

/**
 * This class manages all entities in game
 */
public class EntityManager extends PooledEngine implements Disposable, EntityListener {
  private final PhysicsSystem psychicsSystem;
  private final LanderCPUSystem landerCPUSystem;
  private final LandingSystem landingSystem;
  private SpriteRenderingSystem spriteRenderingSystem;

  public EntityManager(WorldState worldState, LandBot game) {
    super();

    this.spriteRenderingSystem = new SpriteRenderingSystem(game, worldState);
    this.psychicsSystem        = new PhysicsSystem(game, worldState);
    this.landerCPUSystem       = new LanderCPUSystem(game, worldState);
    this.landingSystem         = new LandingSystem(game, worldState);

    addEntityListener(landingSystem);
    addEntityListener(landerCPUSystem);
    addEntityListener(this);
    addEntityListener(psychicsSystem);

    addSystem(landerCPUSystem);
    addSystem(landingSystem);
    addSystem(spriteRenderingSystem);
    addSystem(psychicsSystem);
  }


  @Override
  public void dispose() {
    removeAllEntities();
    clearPools();
    landingSystem.dispose();
    landerCPUSystem.dispose();
    psychicsSystem.dispose();
    spriteRenderingSystem.dispose();
    spriteRenderingSystem = null;
  }

  /**
   * Assign entity to {@link de.macbury.landbot.core.entities.components.PositionComponent}
   * @param entity
   */
  @Override
  public void entityAdded(Entity entity) {
    if (Components.Position.has(entity)) {
      Components.Position.get(entity).entity = entity;
    }
  }

  @Override
  public void entityRemoved(Entity entity) {

  }
}
