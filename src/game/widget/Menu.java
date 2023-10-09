package game.widget;

import game.Game;
import game.io.Keyboard;
import game.util.Util;
import game.event.Event;

import java.awt.*;
import java.time.Duration;
import java.util.List;

public class Menu {
    public Event<CloseEvent> getOnCloseEvent() {
        return onCloseEvent;
    }

    private final Event<CloseEvent> onCloseEvent = new Event<>();
    private final Game game;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private int caret = 0;
    private List<Widget> widgets;
    private int itemDistance;
    private int textSize;
    private boolean visible = true;

    public Menu(Game game, int x, int y, int width, int height) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        itemDistance = game.TILE_SIZE;
        textSize = 12;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setItemDistance(int itemDistance) {
        this.itemDistance = itemDistance;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setWidgets(Widget... widgets) {
        this.widgets = List.of(widgets);
    }

    public void onUpdate(Duration delta) {
        if (getGame().getKeyboard().pressed(Keyboard.SECONDARY)) {
            setVisible(false);
            onCloseEvent.fire(new CloseEvent());
        }

        if (!isVisible()) {
            return;
        }

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

    public Game getGame() {
        return game;
    }

    public void onRender(Graphics2D graphics) {
        if (!isVisible()) {
            return;
        }

        UserInterface ui = new UserInterface(graphics, getGame().SCREEN_WIDTH, getGame().SCREEN_HEIGHT,
                                             getGame().TILE_SIZE
        );

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
