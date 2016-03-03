package de.macbury.landbot.core.graphics;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public interface LodRenderableProvider {
  public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool, Lod lod);
}
