package game.state;

import game.Game;
import game.form.element.FormElement;
import game.form.properties.FormBounds;
import game.form.properties.FormFill;
import game.form.properties.layout.FormGridLayout;

import java.awt.*;
import java.time.Duration;

public class Sandbox extends GameState {

    public static class FormGrid extends FormElement {
        public FormGrid(int x, int y, int width, int height, int cols, int rows) {
            super(x, y, width, height);
            setLayout(new FormGridLayout(cols, rows));

            setFill(new FormFill(Color.BLACK, 25));

            for (int i = 0; i < cols * rows; i++) {
                FormElement child = new FormElement();
                child.setFill(new FormFill(Color.DARK_GRAY, 5));
                addChild(child);
            }

            getLayout().execute(this);

            return;
        }
    }

    FormGrid grid = new FormGrid(25, 25, 50, 50, 10, 10);

    public Sandbox(Game game) {
        super(game);

    }

    @Override
    public void onEnter() {
        getOnGuiRender().listenWith(grid::onRender);
    }

    @Override
    public void onUpdate(Duration delta) {

    }

    @Override
    public void onRender(Graphics2D graphics) {
       getOnGuiRender().fire(graphics);
    }
}
