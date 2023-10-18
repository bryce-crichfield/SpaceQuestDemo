package game.state.battle.hud;

import game.Game;
import game.event.Event;
import game.form.FormConst;
import game.form.element.FormElement;
import game.form.properties.*;
import game.form.properties.layout.FormHorizontalLayout;
import game.state.battle.model.actor.Actor;
import game.util.Util;

import java.awt.*;
import java.util.function.BiConsumer;

public class HudStats extends FormElement {
    public static final int WIDTH = 160;
    public static final int HEIGHT = 200;
    private final Event<Actor> onActorChanged;
    private final FormElement title = Util.pure(() -> {
        FormElement element = new FormElement(WIDTH, 20);
        FormText text = new FormText();
        text.setValue("Stats");
        text.setSize(16);
        element.setText(text);
        element.setHorizontalTextAlignment(FormAlignment.CENTER);

        return element;
    });


    public HudStats(int x, int y, Event<Actor> onActorChanged) {
        super(x, y, WIDTH, HEIGHT);

        this.onActorChanged = onActorChanged;

        formatContainer();

        addChild(title);

    }

    private void formatContainer() {
        Paint gradient = FormConst.screenGradient(FormConst.DarkGray, FormConst.Black);
        setRounding(25);
        setFillPaint(gradient);

        FormBorder border = new FormBorder();
        setBorder(border);
    }
}
