package game.state.title;

import game.Game;
import game.form.element.FormElement;
import game.form.element.FormGrid;
import game.form.element.FormMenu;
import game.form.element.FormSlider;
import game.form.properties.*;
import game.form.properties.layout.FormGridLayout;
import game.form.properties.layout.FormHorizontalLayout;
import game.form.properties.layout.FormVerticalLayout;
import game.io.Keyboard;
import game.state.GameState;
import game.state.battle.BattleState;
import game.state.options.OptionsMenuState;
import game.state.overworld.PlayOverworldState;

import javax.swing.text.html.Option;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class MainMenuState extends GameState {
    StarBackground starBackground;
    FormMenu menu;
    private final int menuWidth = 200;
    private final int menuHeight = 300;

    public MainMenuState(Game game) {
        super(game);
        starBackground = new StarBackground(this, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);

        int menuX = (Game.SCREEN_WIDTH - menuWidth) / 2;
        int menuY = (Game.SCREEN_HEIGHT - menuHeight) / 2;
        menu = new FormMenu(menuX, menuY, menuWidth, menuHeight);
        menu.setLayout(new FormVerticalLayout());
        menu.setMargin(new FormMargin(10, 10, 10, 10));
        menu.setFillPaint(Color.BLACK);
        menu.setRounding(20);

        BiFunction<String, Runnable, FormElement> option = (name, onPrimary) -> {
            FormElement element = new FormElement(100, 20);
            FormText text = new FormText();
            text.setValue(name);
            text.setSize(16);
            element.setText(text);
            element.setHorizontalTextAlignment(FormAlignment.CENTER);
            element.setVerticalTextAlignment(FormAlignment.CENTER);
            element.setVisible(true);
            element.getOnPrimary().listenWith(event -> onPrimary.run());
            return element;
        };

        menu.addCaretChild(option.apply("Battle", () -> {
            getGame().deferred().fire(g -> {
                g.pushState(new BattleState(g));
            });
        }));

        menu.addCaretChild(option.apply("Overworld", () -> {

        }));

        menu.addCaretChild(option.apply("Options", () -> {
            getGame().deferred().fire(g -> {
                g.pushState(new OptionsMenuState(g));
            });
        }));

        menu.addCaretChild(option.apply("Exit", () -> {
            System.exit(0);
        }));

        FormSlider formSlider = new FormSlider(0, 100, 50, 1);
        formSlider.setSize(100, 20);
        menu.addCaretChild(formSlider);
    }

    @Override
    public void onEnter() {
        getSubscriptions().on(getOnGuiRender()).run(starBackground::onRender);
        getSubscriptions().on(getOnGuiRender()).run(menu::onRender);
        getSubscriptions().on(Keyboard.keyPressed).run(menu::onKeyPressed);
    }

    @Override
    public void onUpdate(Duration delta) {
        starBackground.onUpdate(delta);
    }

    @Override
    public void onRender(Graphics2D graphics) {
        getOnGuiRender().fire(graphics);
    }
}
