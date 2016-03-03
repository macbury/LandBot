package de.macbury.landbot.core.ui;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * Renders animated widget
 */
public class AnimatedImage extends Widget {
  private float time;
  private Animation animation;

  public AnimatedImage(Animation animation) {
    this.animation = animation;
    this.animation.setPlayMode(Animation.PlayMode.LOOP);
    this.time      = 0.0f;
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    this.time += delta;
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
    batch.draw(
      animation.getKeyFrame(time),
      this.getX(),
      this.getY()
    );
  }
}
