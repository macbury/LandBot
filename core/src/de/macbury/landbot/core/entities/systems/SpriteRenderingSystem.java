package de.macbury.landbot.core.entities.systems;

import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.landbot.core.entities.Components;
import de.macbury.landbot.core.entities.components.PositionComponent;
import de.macbury.landbot.core.entities.components.SpriteComponent;

/**
 * This system renders all {@link Entity} with {@link SpriteBatch}. Entity must have:
 * {@link PositionComponent}
 * {@link de.macbury.landbot.core.entities.components.SpriteComponent}
 */
public class SpriteRenderingSystem extends IteratingSystem implements Disposable {
  private SpriteBatch spriteBatch;
  private Camera camera;
  private Matrix4 tempMat;

  public SpriteRenderingSystem(Camera camera) {
    super(Family.all(PositionComponent.class, SpriteComponent.class).get());
    this.spriteBatch = new SpriteBatch();
    this.camera      = camera;
    this.tempMat     = new Matrix4();
  }

  @Override
  public void dispose() {
    spriteBatch.dispose();
    spriteBatch = null;
    camera      = null;
  }

  @Override
  public void update(float deltaTime) {
    spriteBatch.setProjectionMatrix(camera.combined);
    spriteBatch.begin(); {
      super.update(deltaTime);
    } spriteBatch.end();
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PositionComponent positionComponent = Components.Position.get(entity);
    SpriteComponent  spriteComponent    = Components.Sprite.get(entity);

    spriteComponent.setCenter(positionComponent.x, positionComponent.z);
    spriteComponent.setRotation(-positionComponent.rotationDeg);//TODO: Rotate in clockwise direction the sprite :/
    spriteComponent.draw(spriteBatch);
  }
}
