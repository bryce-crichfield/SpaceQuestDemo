package game.state;

import game.Game;
import game.form.element.FormElement;
import game.form.element.FormGrid;
import game.form.properties.FormBounds;
import game.form.properties.FormFill;
import game.form.properties.layout.FormGridLayout;

import java.awt.*;
import java.time.Duration;

public class Sandbox extends GameState {
    public Sandbox(Game game) {
        super(game);
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onUpdate(Duration delta) {

    }

    @Override
    public void onRender(Graphics2D graphics) {
       getOnGuiRender().fire(graphics);
    }
}
