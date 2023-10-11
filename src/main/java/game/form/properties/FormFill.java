package game.form.properties;

import lombok.Data;

import java.awt.*;

@Data
public class FormFill {
    private final Paint paint;
    private final Integer roundness;

    public FormFill() {
        this.paint = Color.BLACK;
        this.roundness = 0;
    }

    public FormFill(Paint paint) {
        this.paint = paint;
        this.roundness = 0;
    }

    public FormFill(Integer roundness) {
        this.paint = Color.BLACK;
        this.roundness = roundness;
    }

    public FormFill(Paint paint, Integer roundness) {
        this.paint = paint;
        this.roundness = roundness;
    }

    public void onRender(Graphics2D graphics, FormBounds bounds) {
        graphics.setPaint(paint);
        graphics.fillRoundRect((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(),
                               (int) bounds.getHeight(), roundness, roundness
        );
    }
}
