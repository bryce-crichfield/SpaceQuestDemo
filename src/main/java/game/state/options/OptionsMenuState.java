package game.state.options;

import game.Game;
import game.form.element.*;
import game.form.properties.*;
import game.form.properties.layout.FormHorizontalLayout;
import game.io.Keyboard;
import game.state.GameState;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Duration;

public class OptionsMenuState extends GameState {
    private final FormMenu menu;

    public OptionsMenuState(Game game) {
        super(game);

        menu = new FormMenu(25, 25, 200, 200);

        FormElement musicVolume = new FormElement(100, 20);
        musicVolume.setLayout(new FormHorizontalLayout());
        FormElement music = new FormElement("Music");
        music.getBounds().setWidth(50);
        musicVolume.addChild(music);
        FormElement volume = new FormElement("Volume");
        volume.getBounds().setWidth(50);
        musicVolume.addChild(volume);

        FormElement soundVolume = new FormElement(100, 20);
        soundVolume.setLayout(new FormHorizontalLayout());
        FormElement sound = new FormElement("Sound");
        sound.getBounds().setWidth(50);
        soundVolume.addChild(sound);
        FormElement volume2 = new FormElement("Volume");
        volume2.getBounds().setWidth(50);
        soundVolume.addChild(volume2);

        menu.addCaretChild(musicVolume);
        menu.addCaretChild(soundVolume);
        menu.onLayout();

        menu.setRounding(25);
        Color barelyBlack = new Color(0x21, 0x21, 0x21, 0xff);
        Paint gradient = new GradientPaint(0, 0, barelyBlack, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, Color.BLACK);
        menu.setFillPaint(gradient);
    }


    @Override
    public void onEnter() {
        getSubscriptions().on(Keyboard.keyPressed).run(menu::onKeyPressed);
        getSubscriptions().on(getOnGuiRender()).run(menu::onRender);
    }

    @Override
    public void onUpdate(Duration delta) {

    }

    @Override
    public void onRender(Graphics2D graphics) {
        getOnGuiRender().fire(graphics);
    }
}
