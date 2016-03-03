package de.macbury.landbot.core.entities;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.landbot.LandBot;
import de.macbury.landbot.core.World;
import de.macbury.landbot.core.entities.systems.*;

/**
 * This class manages all entities in game
 */
public class EntityManager extends PooledEngine implements Disposable {
  private SpriteRenderingSystem spriteRenderingSystem;

  public EntityManager(World world, LandBot game) {
    super();

    this.spriteRenderingSystem = new SpriteRenderingSystem(world.camera);

    addSystem(spriteRenderingSystem);
  }

  @Override
  public void dispose() {
    removeAllEntities();
    clearPools();
    spriteRenderingSystem.dispose();
    spriteRenderingSystem = null;
  }
}
