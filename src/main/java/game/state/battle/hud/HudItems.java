package game.state.battle.hud;

import game.form.element.FormElement;
import game.form.element.FormGrid;
import game.form.properties.FormAlignment;
import game.form.properties.FormBorder;
import game.form.properties.FormMargin;

import java.awt.*;
import java.util.function.Supplier;

public class HudItems extends FormGrid {

    public HudItems() {
        super(25, 25, 150, 150, 4,4);
        setFillPaint(Color.DARK_GRAY);
        setRounding(20);
        setBorder(new FormBorder());
        setElementAlignment(FormAlignment.CENTER);
        setMargin(new FormMargin(5, 5, 5, 5));
        setPadding(new FormMargin(5, 5, 5, 5));

        Supplier<FormElement> label = () -> {
            FormElement labelElement = new FormElement(20, 20);
            labelElement.setFillPaint(Color.WHITE);
            labelElement.setRounding(10);
            labelElement.getOnHover().listenWith(Null -> {
                labelElement.setFillPaint(Color.RED);
                labelElement.setRounding(10);
            });
            labelElement.getOnUnhover().listenWith(Null -> {
                labelElement.setFillPaint(Color.WHITE);
                labelElement.setRounding(10);
            });
            return labelElement;
        };

        for (int x = 0 ; x < 5; x++) {
            for (int y = 0 ; y < 5 ; y++) {
                addChild(label.get());
            }
        }

        onLayout();
    }

}
