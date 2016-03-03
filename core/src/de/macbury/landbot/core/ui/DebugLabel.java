package de.macbury.landbot.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Just display every frame the framerate
 */
public class DebugLabel extends Label {
  private float time;

  public DebugLabel(Skin skin) {
    super("", skin, "fpsLabel");
    this.time = 1.0f;
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    time += delta;

    if (time >= 1.0f) {
      time = 0.0f;
      this.setText(
          "FPS: " + Gdx.graphics.getFramesPerSecond() + "\n" +
          "Vertex avg: " + String.valueOf(Math.round(GLProfiler.vertexCount.average)) + "\n" +
          "Draw calls: " + GLProfiler.drawCalls + "\n" +
          "Texture binds: " + GLProfiler.textureBindings + "\n" +
          "Shader switch: " + GLProfiler.shaderSwitches + "\n" +
          "Memory: " + humanReadableByteCount(Gdx.app.getNativeHeap(), true) + "/" + humanReadableByteCount(Gdx.app.getJavaHeap(), true)
      );


    }
    GLProfiler.reset();
  }

  public static String humanReadableByteCount(long bytes, boolean si) {
    int unit = si ? 1000 : 1024;
    if (bytes < unit) return bytes + " B";
    int exp = (int) (Math.log(bytes) / Math.log(unit));
    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
  }
}
