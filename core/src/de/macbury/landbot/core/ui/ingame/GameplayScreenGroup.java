package de.macbury.landbot.core.ui.ingame;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.landbot.LandBot;
import de.macbury.landbot.core.WorldState;
import de.macbury.landbot.core.entities.Messages;
import de.macbury.landbot.core.ui.Hud;
import de.macbury.landbot.core.ui.console.GameConsoleView;
import de.macbury.landbot.core.ui.ingame.controller.ProgramingController;

/**
 * Main screen group that contains all ui
 */
public class GameplayScreenGroup extends Group implements Disposable {
  private Hud hud;
  private ProgramingController programingController;
  private Messages messages;

  public GameplayScreenGroup(LandBot game, WorldState worldState) {
    super();
    this.hud      = game.hud;
    this.messages = game.messages;
    this.programingController = new ProgramingController(messages, this, hud.getSkin());


    GameConsoleView consoleView = new GameConsoleView(messages, hud.getSkin());
    consoleView.setWidth(480);
    consoleView.setHeight(320);
    consoleView.setVisible(true);
    addActor(consoleView);
  }

  @Override
  public void dispose() {
    programingController.dispose();
    this.messages = null;
    this.hud      = null;

    for (Actor actor : getChildren()) {
      if (Disposable.class.isInstance(actor)) {
        ((Disposable)actor).dispose();
      }
    }
    remove();
  }
}
