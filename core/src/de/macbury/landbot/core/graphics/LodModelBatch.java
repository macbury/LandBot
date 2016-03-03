package de.macbury.landbot.core.graphics;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;

/**
 * Extended model batch for support of {@link Lod}
 */
public class LodModelBatch extends ModelBatch {

  public void render(final LodRenderableProvider provider, Environment env, final Lod levelOfDetails) {
    final int offset = renderables.size;
    provider.getRenderables(renderables, renderablesPool, levelOfDetails);
    for (int i = offset; i < renderables.size; i++) {
      Renderable renderable   = renderables.get(i);
      renderable.environment  = env;
      renderable.shader       = shaderProvider.getShader(renderable);
    }
  }
}
