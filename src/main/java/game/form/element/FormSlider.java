package game.form.element;

import game.event.Event;

import game.form.properties.FormBorder;
import game.form.properties.FormBounds;
import game.io.Keyboard;
import game.util.Util;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.awt.*;

@Data
@RequiredArgsConstructor
public class FormSlider extends FormElement {
    private final Event<Float> onValueChange = new Event<>();
    private float value;
    private float min;
    private float max;
    private float delta;

    public FormSlider(float min, float max, float value, float delta) {
        this.min = min;
        this.max = max;
        this.value = value;
        this.delta = delta;

        this.setBorder(new FormBorder());

        getOnKeyPressed().listenWith(keycode -> {
            if (keycode == Keyboard.LEFT) {
                this.value -= delta;
            }

            if (keycode == Keyboard.RIGHT) {
                this.value += delta;
            }

            this.value = Util.clamp(this.value, min, max);
        });

        setFillPaint(Color.RED);
    }

    @Override
    public void onRender(Graphics2D graphics) {
        if (!getVisible()) {
            return;
        }

        float percentage = (value - min) / (max - min);
        FormBounds formArea = new FormBounds(0, 0, percentage, 1);
        this.setFillArea(formArea);

        super.onRender(graphics);
    }
}
