package de.macbury.landbot.core.entities;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.landbot.LandBot;
import de.macbury.landbot.core.WorldState;
import de.macbury.landbot.core.entities.systems.*;

/**
 * This class manages all entities in game
 */
public class EntityManager extends PooledEngine implements Disposable {
  private final PhysicsSystem psychicsSystem;
  private SpriteRenderingSystem spriteRenderingSystem;

  public EntityManager(WorldState worldState, LandBot game) {
    super();

    this.spriteRenderingSystem = new SpriteRenderingSystem(game, worldState);
    this.psychicsSystem        = new PhysicsSystem(game, worldState);

    addEntityListener(psychicsSystem);
    addSystem(spriteRenderingSystem);
    addSystem(psychicsSystem);
  }

  @Override
  public void dispose() {
    removeAllEntities();
    clearPools();
    psychicsSystem.dispose();
    spriteRenderingSystem.dispose();
    spriteRenderingSystem = null;
  }
}
