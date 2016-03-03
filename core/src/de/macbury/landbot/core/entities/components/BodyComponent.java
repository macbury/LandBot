package de.macbury.landbot.core.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Pool;

/**
 * This class contains all information aboud box2d {@link Body}
 */
public class BodyComponent implements Pool.Poolable, Component {
  public Body body;
  public BodyDef bodyDef;
  public FixtureDef fixtureDef;

  @Override
  public void reset() {
    body = null;
    bodyDef = null;
    fixtureDef = null;
  }
}
