package de.macbury.landbot.core.entities.systems;

import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.landbot.LandBot;
import de.macbury.landbot.core.WorldState;
import de.macbury.landbot.core.entities.Components;
import de.macbury.landbot.core.entities.components.PositionComponent;
import de.macbury.landbot.core.entities.components.SpriteComponent;
import de.macbury.landbot.core.graphics.framebuffer.Fbo;

/**
 * This system renders all {@link Entity} with {@link SpriteBatch}. Entity must have:
 * {@link PositionComponent}
 * {@link de.macbury.landbot.core.entities.components.SpriteComponent}
 */
public class SpriteRenderingSystem extends IteratingSystem implements Disposable {
  private LandBot game;
  private WorldState worldState;
  private SpriteBatch spriteBatch;
  private Matrix4 tempMat;

  public SpriteRenderingSystem(LandBot game, WorldState worldState) {
    super(Family.all(PositionComponent.class, SpriteComponent.class).get());
    this.spriteBatch = new SpriteBatch();
    this.tempMat     = new Matrix4();
    this.worldState  = worldState;
    this.game        = game;
  }

  @Override
  public void dispose() {
    spriteBatch.dispose();
    spriteBatch = null;
    worldState  = null;
    game        = null;
  }

  @Override
  public void update(float deltaTime) {
    game.fb.begin(Fbo.FinalResult); {
      Gdx.gl.glClearColor(0,0,0,1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      spriteBatch.setProjectionMatrix(worldState.camera.combined);
      spriteBatch.begin(); {
        super.update(deltaTime);
      } spriteBatch.end();
    } game.fb.end();
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PositionComponent positionComponent = Components.Position.get(entity);
    SpriteComponent  spriteComponent    = Components.Sprite.get(entity);

    spriteComponent.setCenter(positionComponent.x, positionComponent.y);
    //spriteComponent.setRotation(-positionComponent.rotation);//TODO: Rotate in clockwise direction the sprite :/
    spriteComponent.draw(spriteBatch);
  }
}
