package game;

import widget.ButtonWidget;
import widget.LabelWidget;
import widget.Widget;

import java.awt.*;
import java.time.Duration;

public class ControlsMenuState extends GameState {
    private final ButtonWidget back;
    private final Widget[] widgets;

    public ControlsMenuState(Game game) {
        super(game);

        back = new ButtonWidget("Back", getGame());

        widgets = new Widget[]{
                new LabelWidget("Move Up", "W", getGame()),
                new LabelWidget("Move Down", "S", getGame()),
                new LabelWidget("Move Left", "A", getGame()),
                new LabelWidget("Move Right", "D", getGame()),
                new LabelWidget("Attack", "Space", getGame()),
                new LabelWidget("Use Item", "E", getGame()),
                new LabelWidget("Pause", "P", getGame()),
        };
    }

    @Override
    public void onUpdate(Duration delta) {
        for (Widget widget : widgets) {
            widget.onUpdate(delta);
        }
        back.onUpdate(delta);

        if (back.pressed) {
            getGame().popState();
        }
    }

    @Override
    public void onRender(Graphics2D graphics) {
        graphics.setColor(UserInterface.background);
        graphics.fillRect(0, 0, getGame().SCREEN_WIDTH, getGame().SCREEN_HEIGHT);

        UserInterface ui = new UserInterface(graphics, getGame().SCREEN_WIDTH, getGame().SCREEN_HEIGHT,
                                             getGame().TILE_SIZE
        );

        int menuWidth = 11 * ui.tileSize;
        int menuHeight = 7 * ui.tileSize;
        int menuX = (ui.screenWidth / 2) - (menuWidth / 2);
        int menuY = (ui.screenHeight / 2) - (menuHeight / 2);
        menuY += ui.tileSize;

        ui.textSize = 24;
        ui.textColor = UserInterface.highlight;
        ui.drawTextCentered("Controls", 0, 16, ui.screenWidth, 32);


        // Draw the Menu Box
        ui.textColor = Color.WHITE;
        ui.drawPanel(menuX, menuY, menuWidth, menuHeight);


        ui.textSize = 12;
        int y = menuY;
        for (Widget widget : widgets) {
            widget.onRender(ui, menuX, y, menuWidth, false);
            y += ui.tileSize / 2;
        }

        back.onRender(ui, menuX, menuY + menuHeight - ui.tileSize - 16, menuWidth, true);
    }
}
