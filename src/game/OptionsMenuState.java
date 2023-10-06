package game;

import widget.ButtonWidget;
import widget.RadioButtonWidget;
import widget.SliderWidget;
import widget.Widget;

import java.awt.*;
import java.time.Duration;
import java.util.List;

public class OptionsMenuState extends GameState {
    private final List<Widget> widgets;
    private int caret = 0;

    private SliderWidget musicVolume;
    private SliderWidget soundVolume;
    private RadioButtonWidget fullscreen;
    private ButtonWidget controls;
    private ButtonWidget back;


    public OptionsMenuState(Game game) {
        super(game);

        musicVolume = new SliderWidget("Music Volume", getGame());
        soundVolume = new SliderWidget("Sound Volume", getGame());
        fullscreen = new RadioButtonWidget("Fullscreen", getGame());
        controls = new ButtonWidget("Controls", getGame());
        back = new ButtonWidget("Back", getGame());

        widgets = List.of(
                musicVolume,
                soundVolume,
                fullscreen,
                controls,
                back
        );
    }

    @Override
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

        if (back.pressed) {
            getGame().popState();
        }

        if (controls.pressed) {
            getGame().pushState(new ControlsMenuState(getGame()));
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
        ui.drawTextCentered("Options Menu", 0, 16, ui.screenWidth, 32);


        // Draw the Menu Box
        ui.textColor = Color.WHITE;
        ui.drawPanel(menuX, menuY, menuWidth, menuHeight);

        ui.textSize = 12;
        int optionsStartY = menuY;
        int y = optionsStartY;
        for (Widget widget : widgets) {
            boolean selected = widgets.indexOf(widget) == caret;
            widget.onRender(ui, menuX, y, menuWidth, selected);
            y += ui.tileSize;
        }

        int caretY = optionsStartY + caret * ui.tileSize;
        ui.drawTextRightJustified(">", menuX, caretY, menuWidth, 32, 12);
    }
}
