package de.macbury.landbot.core.graphics.framebuffer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;


/**
 * Created by macbury on 20.07.15.
 */
public class DebugFrameBufferResult extends Actor {

  private final TextureRegion region;
  private Fbo fbo;
  private FrameBufferManager fb;

  public DebugFrameBufferResult(Fbo fbo, FrameBufferManager frameBufferManager) {
    region = new TextureRegion();
    setWidth(Gdx.graphics.getWidth());
    setHeight(Gdx.graphics.getHeight());
    this.fbo = fbo;
    this.fb = frameBufferManager;
    setZIndex(1);
  }


  /**
   * Remove relation to {@link FrameBufferManager} on {@link Stage#dispose()}
   * @param stage
   */
  @Override
  protected void setStage(Stage stage) {
    super.setStage(stage);
    if (stage == null)
      fb = null;
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
    FrameBuffer frameBuffer = fb.get(fbo);
    region.setTexture(frameBuffer.getColorBufferTexture());
    region.setRegion(0, 0, frameBuffer.getColorBufferTexture().getWidth(), frameBuffer.getColorBufferTexture().getHeight());
    region.flip(false, true);

    batch.draw(region, getX(), getY(), getWidth(), getHeight());
  }

  public static DebugFrameBufferResult build(Fbo fbo, int size, float x, float y, FrameBufferManager frameBufferManager) {
    DebugFrameBufferResult colorResult = new DebugFrameBufferResult(fbo,frameBufferManager);
    colorResult.setWidth(size);
    colorResult.setHeight(size);
    colorResult.setX(x);
    colorResult.setY(y);
    return colorResult;
  }
}
