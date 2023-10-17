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

        menu = new FormMenu(25, 25, 50, 50);

        FormElement musicVolume = new FormElement(100, 1);
        musicVolume.setLayout(new FormHorizontalLayout());
        FormElement music = new FormElement("Music");
        music.getBounds().setWidth(50);
        musicVolume.addChild(music);
        FormElement volume = new FormElement("Volume");
        volume.getBounds().setWidth(50);
        musicVolume.addChild(volume);

        FormElement soundVolume = new FormElement(100, 1);
        soundVolume.setLayout(new FormHorizontalLayout());
        FormElement sound = new FormElement("Sound");
        sound.getBounds().setWidth(50);
        soundVolume.addChild(sound);
        FormElement volume2 = new FormElement("Volume");
        volume2.getBounds().setWidth(50);
        soundVolume.addChild(volume2);

        menu.addChild(musicVolume);
        menu.addChild(soundVolume);
        menu.onLayout();

        menu.setRounding(25);
        menu.setFillPaint(getPaint(menu));
    }

    @NotNull
    private Paint getPaint(FormElement element) {
        int menuX = (int) element.getBounds().getX();
        int menuWidth = (int) element.getBounds().getWidth();
        int menuY = (int) element.getBounds().getY();
        int menuHeight = (int) element.getBounds().getHeight();

        Paint paint = new GradientPaint(menuX, menuY, Color.BLACK, menuX + menuWidth, menuY + menuHeight,
                                        Color.DARK_GRAY
        );
        return paint;
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
