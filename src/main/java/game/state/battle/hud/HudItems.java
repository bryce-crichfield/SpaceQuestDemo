package game.state.battle.hud;

import game.form.element.FormElement;
import game.form.element.FormGrid;
import game.form.properties.FormAlignment;
import game.form.properties.FormBorder;
import game.form.properties.FormFill;
import game.form.properties.FormMargin;

import java.awt.*;
import java.util.function.Supplier;

public class HudItems extends FormGrid {

    public HudItems() {
        super(25, 25, 150, 150, 5, 5);
        setFill(new FormFill(Color.DARK_GRAY, 20));
        setBorder(new FormBorder());
        setElementAlignment(FormAlignment.CENTER);
        setMargin(new FormMargin(5, 5, 5, 5));
        setPadding(new FormMargin(5, 5, 5, 5));

        Supplier<FormElement> label = () -> {
            FormElement labelElement = new FormElement(20, 20);
            labelElement.setFill(new FormFill(Color.WHITE, 15));
            labelElement.getOnPrimary().listenWith(Null -> {
                labelElement.setFill(new FormFill(Color.RED, 15));
            });
            return labelElement;
        };

        for (int x = 0 ; x < 5; x++) {
            for (int y = 0 ; y < 5 ; y++) {
                addChild(label.get());
            }
        }


        getLayout().execute(this);
    }

}
