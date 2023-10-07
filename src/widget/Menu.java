package widget;

import game.Game;
import game.Keyboard;
import game.UserInterface;
import game.Util;

import java.awt.*;
import java.time.Duration;
import java.util.List;

public class Menu {
    private final Game game;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private int caret = 0;
    private List<Widget> widgets;
    private int itemDistance;
    private int textSize;

    public Menu(Game game, int x, int y, int width, int height) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        itemDistance = game.TILE_SIZE;
        textSize = 12;
    }

    public void setItemDistance(int itemDistance) {
        this.itemDistance = itemDistance;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public Game getGame() {
        return game;
    }

    public void setWidgets(Widget... widgets) {
        this.widgets = List.of(widgets);
    }

    public void onUpdate(Duration delta) {
        if (getGame().getKeyboard().pressed(Keyboard.UP)) {
            caret--;
            getGame().getAudio().play("caret.wav");
        }

        if (getGame().getKeyboard().pressed(Keyboard.DOWN)) {
            caret++;
            getGame().getAudio().play("caret.wav");
        }
        caret = Util.wrap(caret, 0, widgets.size());

        Widget currentWidget = widgets.get(caret);
        currentWidget.onUpdate(delta);
    }

    public void onRender(Graphics2D graphics) {
        UserInterface ui = new UserInterface(graphics, getGame().SCREEN_WIDTH, getGame().SCREEN_HEIGHT, getGame().TILE_SIZE);

        int menuWidth = width;
        int menuX = x;
        int menuY = y;
        menuY += ui.tileSize;

        // Draw the Menu Box
        ui.textColor = Color.WHITE;
        ui.drawPanel(menuX, menuY, menuWidth, height);

        ui.textSize = textSize;
        int optionsStartY = menuY;
        int y = optionsStartY;
        for (Widget widget : widgets) {
            boolean selected = widgets.indexOf(widget) == caret;
            widget.onRender(ui, menuX, y, menuWidth, selected);
            y += itemDistance;
        }

        int caretY = optionsStartY + caret * itemDistance;
        ui.drawTextRightJustified(">", menuX, caretY, menuWidth, 32, 12);
    }
}
