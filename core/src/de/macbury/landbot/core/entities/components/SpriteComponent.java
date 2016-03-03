package de.macbury.landbot.core.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool;

/**
 * This component contains all information required for rendering sprites
 */
public class SpriteComponent extends Sprite implements Component, Pool.Poolable  {
  @Override
  public void reset() {
    setTexture(null);
  }
}
