package game.form.properties;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.With;

import java.awt.*;

@Data
@With
@RequiredArgsConstructor
public class FormBorder {
    private final Color inlayColor;
    private final Color outlineColor;
    private final Integer rounding;
    private final Integer thicknessInlay;
    private final Integer thicknessOutline;
    private final FormLine lineStyle;

    public FormBorder() {
        this(Color.WHITE, Color.BLACK, 25, 3, 6, FormLine.SOLID);
    }

    public void onRender(Graphics2D graphics, FormBounds bounds) {
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();
        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();

        Stroke stroke = graphics.getStroke();
        Paint paint = graphics.getPaint();

        graphics.setStroke(new BasicStroke(thicknessOutline, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.setPaint(outlineColor);
        graphics.drawRoundRect(x, y, width, height, rounding, rounding);

        graphics.setStroke(new BasicStroke(thicknessInlay, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.setPaint(inlayColor);
        graphics.drawRoundRect(x, y, width, height, rounding, rounding);

        graphics.setStroke(stroke);
        graphics.setPaint(paint);
    }
}
