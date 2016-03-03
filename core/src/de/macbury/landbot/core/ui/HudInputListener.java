package de.macbury.landbot.core.ui;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Disposable;

/**
 * This class extends {@link InputListener} and automaticaly subscribe to {@link Hud#getOverlay()}
 */
public class HudInputListener extends InputListener implements Disposable {

  protected Hud hud;

  public HudInputListener(Hud hud) {
    this.hud = hud;
    hud.getOverlay().addListener(this);
  }

  @Override
  public void dispose() {
    hud.getOverlay().removeListener(this);
    this.hud = null;
  }
}
