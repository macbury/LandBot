package de.macbury.landbot.core.graphics.framebuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * This actor renders on screen content of framebuffer
 */
public class FullScreenFrameBufferResult extends Actor {

  private final TextureRegion region;
  private Fbo fbo;
  private FrameBufferManager frameBufferManager;

  public FullScreenFrameBufferResult(Fbo fbo, FrameBufferManager frameBufferManager) {
    region = new TextureRegion();
    this.fbo = fbo;
    this.frameBufferManager = frameBufferManager;
    setWidth(Gdx.graphics.getWidth());
    setHeight(Gdx.graphics.getHeight());
    setZIndex(1);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Texture tex = frameBufferManager.get(fbo).getColorBufferTexture();
    region.setTexture(tex);
    region.setRegion(0, 0, tex.getWidth(), tex.getHeight());
    region.flip(false, true);
    batch.setColor(getColor());
    batch.draw(region, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  }

  /**
   * Remove relation to {@link FrameBufferManager} on {@link Stage#dispose()}
   * @param stage
   */
  @Override
  protected void setStage(Stage stage) {
    super.setStage(stage);
    if (stage == null)
      frameBufferManager = null;
  }
}
