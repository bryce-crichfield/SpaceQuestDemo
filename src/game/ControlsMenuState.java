package game;

import widget.ButtonWidget;
import widget.LabelWidget;
import widget.Menu;

import java.awt.*;
import java.time.Duration;

public class ControlsMenuState extends GameState {
    private final Menu menu;

    public ControlsMenuState(Game game) {
        super(game);

        int menuWidth = 11 * getGame().TILE_SIZE;
        int menuHeight = 7 * getGame().TILE_SIZE;
        int menuX = (getGame().SCREEN_WIDTH / 2) - (menuWidth / 2);
        int menuY = (getGame().SCREEN_HEIGHT / 2) - (menuHeight / 2);

        menu = new Menu(getGame(), menuX, menuY, menuWidth, menuHeight);

        menu.setWidgets(
                new LabelWidget("Move Up", "W", getGame()),
                new LabelWidget("Move Down", "S", getGame()),
                new LabelWidget("Move Left", "A", getGame()),
                new LabelWidget("Move Right", "D", getGame()),
                new LabelWidget("Attack", "Space", getGame()),
                new LabelWidget("Use Item", "E", getGame()),
                new LabelWidget("Pause", "P", getGame()),
                new ButtonWidget("Back", getGame(), () -> {
                    getGame().popState();
                })
        );

        menu.setItemDistance(16);
    }

    @Override
    public void onUpdate(Duration delta) {
        menu.onUpdate(delta);
    }

    @Override
    public void onRender(Graphics2D graphics) {
        graphics.setColor(UserInterface.background);
        graphics.fillRect(0, 0, getGame().SCREEN_WIDTH, getGame().SCREEN_HEIGHT);

        UserInterface ui = new UserInterface(graphics, getGame().SCREEN_WIDTH, getGame().SCREEN_HEIGHT,
                getGame().TILE_SIZE
        );

        ui.textSize = 24;
        ui.textColor = UserInterface.highlight;
        ui.drawTextCentered("Controls", 0, 16, ui.screenWidth, 32);

        menu.onRender(graphics);
    }
}
