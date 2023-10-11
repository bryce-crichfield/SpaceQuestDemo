package game.state.options;

import game.Game;
import game.state.GameState;
import game.widget.Menu;
import game.widget.*;

import java.awt.*;
import java.time.Duration;

public class ControlsMenuState extends GameState {
    private final Menu menu;

    public ControlsMenuState(Game game) {
        super(game);

        int menuWidth = 11 * Game.TILE_SIZE;
        int menuHeight = 7 * Game.TILE_SIZE;
        int menuX = (Game.SCREEN_WIDTH / 2) - (menuWidth / 2);
        int menuY = (Game.SCREEN_HEIGHT / 2) - (menuHeight / 2);

        menu = new Menu(getGame(), menuX, menuY, menuWidth, menuHeight);

        menu.setWidgets(
                new LabelWidget("Move Up", "W", getGame()),
                new LabelWidget("Move Down", "S", getGame()),
                new LabelWidget("Move Left", "A", getGame()),
                new LabelWidget("Move Right", "D", getGame()),
                new LabelWidget("Attack", "Space", getGame()),
                new LabelWidget("Use Item", "E", getGame()),
                new LabelWidget("Pause", "P", getGame()),
                new Blank(getGame()),
                new Blank(getGame()),
                new Blank(getGame()),
                new ButtonWidget("Back", getGame(), () -> {
                    getGame().popState();
                })
        );

        menu.setItemDistance(16);
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onUpdate(Duration delta) {
        menu.onUpdate(delta);
    }

    @Override
    public void onRender(Graphics2D graphics) {


        UserInterface ui = new UserInterface(graphics, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT,
                                             Game.TILE_SIZE
        );

        graphics.setColor(ui.background);
        graphics.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);

        ui.textSize = 24;
        ui.textColor = ui.highlight;
        ui.drawTextCentered("Controls", 0, 16, ui.screenWidth, 32);

        menu.onRender(graphics);
    }
}
