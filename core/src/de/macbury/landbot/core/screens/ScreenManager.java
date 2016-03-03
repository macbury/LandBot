package de.macbury.landbot.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.macbury.landbot.LandBot;
import de.macbury.landbot.core.assets.Assets;

/**
 * Manages in game screens.
 */
public class ScreenManager implements Disposable {
  private static final String TAG = "ScreenManager";
  public final LandBot game;
  private final DefaultStateMachine<ScreenManager, ScreenManagerState> stateMachine;
  private final Assets assets;
  private ScreenBase currentScreen;
  private ScreenBase nextScreen;

  public ScreenManager(LandBot game) {
    this.game         = game;
    this.assets       = game.assets;
    this.stateMachine = new DefaultStateMachine<ScreenManager, ScreenManagerState>(this, ScreenManagerState.Runtime);
  }

  /**
   * Update all screens
   */
  public void update() {
    stateMachine.update();
  }

  /**
   * Fade out current screen, load assets for next screen and gently fade in
   * @param switchToScreen
   */
  public void set(ScreenBase switchToScreen) {
    if (stateMachine.getCurrentState() != ScreenManagerState.Runtime) {
      throw new GdxRuntimeException("Cannot change screen while: " + stateMachine.getCurrentState());
    }
    nextScreen = switchToScreen;
    stateMachine.changeState(ScreenManagerState.Hide);
  }

  @Override
  public void dispose() {
    if (currentScreen != null)
      currentScreen.dispose();
    currentScreen = null;
  }

  private enum ScreenManagerState implements State<ScreenManager> {
    Hide {
      @Override
      public void enter(ScreenManager manager) {
        manager.nextScreen.link(manager.game);
        manager.nextScreen.preload();

        if (manager.currentScreen != null) {
          manager.currentScreen.dispose();
          manager.currentScreen.unlink();
        }

        manager.stateMachine.changeState(ScreenManagerState.Loading);
      }

      @Override
      public void exit(ScreenManager manager) {
        manager.currentScreen = null;
      }
    },
    Loading {
      @Override
      public void enter(ScreenManager manager) {
        manager.game.hud.showLoading();
      }

      @Override
      public void exit(ScreenManager manager) {
        manager.game.hud.hideLoading();
      }

      @Override
      public void update(ScreenManager manager) {
        if (manager.assets.update()) {
          manager.stateMachine.changeState(ScreenManagerState.Show);
        }
      }
    },
    Show {
      @Override
      public void enter(ScreenManager manager) {
        manager.currentScreen = manager.nextScreen;
        manager.currentScreen.create();
        manager.currentScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        manager.nextScreen    = null;
        manager.stateMachine.changeState(ScreenManagerState.Runtime);
      }
    },
    Runtime {
      @Override
      public void update(ScreenManager manager) {
        if (manager.currentScreen != null)
          manager.currentScreen.render(Gdx.graphics.getDeltaTime());
      }
    };

    @Override
    public void enter(ScreenManager entity) {

    }

    @Override
    public void update(ScreenManager entity) {

    }

    @Override
    public void exit(ScreenManager entity) {

    }

    @Override
    public boolean onMessage(ScreenManager entity, Telegram telegram) {
      return false;
    }
  }
}